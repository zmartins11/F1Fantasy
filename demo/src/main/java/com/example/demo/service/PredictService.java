package com.example.demo.service;

import com.example.demo.dto.NextRaceInfoDto;
import com.example.demo.dto.PointsInfoDto;
import com.example.demo.dto.PredictionDto;
import com.example.demo.dto.TotalPointsDto;
import com.example.demo.enums.Formula1DriverEnum;
import com.example.demo.model.fantasy.*;
import com.example.demo.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PredictService {

    @Autowired
    private RaceResultRepository raceResultRepository;
    @Autowired
    private PredictRepository predictRepository;
    @Autowired
    private ErgastService ergastService;
    @Autowired
    private PredictionResultRepository predictionResultRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DriversPointsRepository driversPointsRepository;

    private final static String SEASON_2023 = "2023";
    private final static String SEASON_2022 = "2022";

    public RaceResult findBySeasonAndRound(String season, String round) {
        return raceResultRepository.findBySeasonAndRound(season, round);
    }


    public void saveRace(RaceResult raceResult) {

        RaceResult existingRaceResult = raceResultRepository.findBySeasonAndRound(raceResult.getSeason(), String.valueOf(raceResult.getRound()));
        if(existingRaceResult != null) {
            return;
        }
        raceResultRepository.save(raceResult);
    }

    public RaceResult getRace(String season, String round) {
        return findBySeasonAndRound(season, round);
    }

    public boolean checkRaceFinished(String season, String round) {
        RaceResult race = getRace(season, round);
        return race.isRaceFinished();
    }

    public Prediction savePrediction(PredictionDto prediction) {
        //nova prediction :
        // 1- if podium != null
        // 2- if fastestLap != null
        Prediction predictionSaved = predictRepository.findByUserIdAndRound(prediction.getUser(), prediction.getRound());
        if (predictionSaved != null) {
            if (prediction.getFirst() != null) {
                predictionSaved.setFirst(prediction.getFirst());
                predictionSaved.setSecond(prediction.getSecond());
                predictionSaved.setThird(prediction.getThird());
                predictionSaved.setPredictedPodium(Boolean.TRUE);
            }
            if (prediction.getFastestLap() != null) {
                predictionSaved.setFastestLap(prediction.getFastestLap());
                predictionSaved.setPredictedFastestLap(Boolean.TRUE);
            }
            return predictRepository.save(predictionSaved);
        } else {
            Prediction newPrediction = new Prediction();
            newPrediction.setUserId(prediction.getUser());
            newPrediction.setRound(prediction.getRound());
            newPrediction.setPredictedFastestLap(Boolean.FALSE);
            newPrediction.setPredictedPodium(Boolean.FALSE);
            if (prediction.getFirst() != null) {
                newPrediction.setFirst(prediction.getFirst());
                newPrediction.setSecond(prediction.getSecond());
                newPrediction.setThird(prediction.getThird());
                newPrediction.setPredictedPodium(Boolean.TRUE);
            }
            if (prediction.getFastestLap() != null) {
                newPrediction.setFastestLap(prediction.getFastestLap());
                newPrediction.setPredictedFastestLap(Boolean.TRUE);
            }
            RaceResult race = raceResultRepository.findByRound(prediction.getRound());
            newPrediction.setRaceId(race.getId().toString());
            return predictRepository.save(newPrediction);
        }
    }

    // método chamado quando o RaceResult com o race_id da prediction é preenchido
    public int calculate(Prediction prediction, RaceResult raceResult) throws JsonProcessingException {
        int points = 0;
        HashMap<Integer, Integer> resultsPercentage = new HashMap<>();
        int racesCurrentSeason = getSeasonRaces(SEASON_2023);

        if ((!prediction.getFirst().equals(raceResult.getFirst())) &&
                (!prediction.getSecond().equals(raceResult.getSecond())) &&
                (!prediction.getThird().equals(raceResult.getThird()))) {

            createDriversPoints(prediction.getFirst() ,raceResult.getId(), points);
            createDriversPoints(prediction.getSecond(),raceResult.getId(), points);
            createDriversPoints(prediction.getThird(), raceResult.getId(), points);

        }

        points = processDriver("1", raceResult.getId(), prediction.getFirst(), raceResult.getFirst(), points, racesCurrentSeason);
        points = processDriver("2", raceResult.getId(),prediction.getSecond(), raceResult.getSecond(), points, racesCurrentSeason);
        points = processDriver("3", raceResult.getId(), prediction.getThird(), raceResult.getThird(), points, racesCurrentSeason);

        return points;
    }

    private int processDriver(String position, Integer raceId, String predictedDriver, String raceResultDriver, int points, int racesCurrentSeason) throws JsonProcessingException {
        if (predictedDriver.equals(raceResultDriver)) {
            Map<Integer, Integer> resultsPercentage = ergastService.testApiGetResult(position, racesCurrentSeason, Formula1DriverEnum.getNameByNumber(Integer.parseInt(raceResultDriver)));
            Iterator<Map.Entry<Integer, Integer>> iterator = resultsPercentage.entrySet().iterator();
            if (iterator.hasNext()) {
                Map.Entry<Integer, Integer> firstEntry = iterator.next();
                int percentage = (int)((double) firstEntry.getKey() / firstEntry.getValue() * 100);
                if (percentage >= 75) {
                    points += 1;
                    createDriversPoints(predictedDriver,raceId, 1);
                } else if (percentage >= 50) {
                    points += 3;
                    createDriversPoints(predictedDriver,raceId, 3);
                } else if (percentage >= 25) {
                    points += 5;
                    createDriversPoints(predictedDriver,raceId, 5);
                } else {
                    points += 10;
                    createDriversPoints(predictedDriver,raceId, 10);
                }
            }
        } else {
            createDriversPoints(predictedDriver,raceId,0);
        }
        return points;
    }

    private void createDriversPoints(String driver, Integer raceResultId, int points) {
        DriversPoints driversPoints = new DriversPoints();
         if (driversPointsRepository.findByRaceIdAndDriver(String.valueOf(raceResultId), driver).isEmpty()) {
             driversPoints.setDriver(driver);
             driversPoints.setRaceId(raceResultId);
             driversPoints.setPoints(points);
             driversPointsRepository.save(driversPoints);
         };
    }


    public int getSeasonRaces(String season) {
        List<RaceResult> racesBySeason = raceResultRepository.findBySeason(season);
        return racesBySeason.size();
    }

    // metodo devera sr chamado atraves de cronjob aquando ultima corrida terminada
    // adicionar coluna boolean : calculado
    public void testCalculate(String user, String round, String season) throws JsonProcessingException {
        RaceResult race  = raceResultRepository.findBySeasonAndRound(season, round);
        Prediction prediction = null;
        if (race.getFirst() != null) {
            prediction = predictRepository.findByUserIdAndRaceId(user, String.valueOf(race.getId()));
            int pointsPrediction = calculate(prediction, race);
            PredictionResult predictionResult = new PredictionResult();
            predictionResult.setUserId(user);
            predictionResult.setRaceId(String.valueOf(race.getId()));
            predictionResult.setPoints(pointsPrediction);
            predictionResultRepository.save(predictionResult);
        }
        //TODO : save points
    }

    public RaceResult getNextRaceInfo() {
        return raceResultRepository.findTopByRaceFinishedFalseOrderByRoundAsc();
    }

    public RaceResult getRacePassed() {
        return raceResultRepository.findTopByRaceFinishedTrueOrderByRoundDesc();
    }

    public void updatePredictionLocked(NextRaceInfoDto nextRaceInfo) {
        RaceResult raceResult = raceResultRepository.findByRound(nextRaceInfo.getRound());
        raceResult.setPredictionLocked(nextRaceInfo.getPredictionLocked());
        raceResultRepository.save(raceResult);

    }

    public NextRaceInfoDto getUserPrediction(NextRaceInfoDto nextRaceInfoDto, String username) {
        Prediction userPrediction = predictRepository.findByUserIdAndRound(username, nextRaceInfoDto.getRound());
        if (userPrediction != null) {
            nextRaceInfoDto.setUserHavePrediction(Boolean.TRUE);
            nextRaceInfoDto.setFirst(userPrediction.getFirst());
            nextRaceInfoDto.setSecond(userPrediction.getSecond());
            nextRaceInfoDto.setThird(userPrediction.getThird());
            if (userPrediction.getPredictedPodium()) {
                nextRaceInfoDto.setFastestLap(userPrediction.getFastestLap());
            }
            nextRaceInfoDto.setPredictedPodium(userPrediction.getPredictedPodium());
            nextRaceInfoDto.setPredictedFastestLap(userPrediction.getPredictedFastestLap());
        } else {
            nextRaceInfoDto.setUserHavePrediction(Boolean.FALSE);
        }
        return nextRaceInfoDto;
    }

    public TotalPointsDto getTotalPointsByUser(String username) {
        TotalPointsDto totalPointsDto = new TotalPointsDto();
        Long points = predictionResultRepository.sumPointsByUserId(username);
        if (points==null) {
            points = 0L;
        }

        totalPointsDto.setUsername(username);
        totalPointsDto.setPoints(String.valueOf(points));

        return totalPointsDto;
    }

    public List<TotalPointsDto> getTotalPoints() {
        List<TotalPointsDto> listUsers = new ArrayList<>();
        List<Object[]> result = predictionResultRepository.findTotalPointsByUser();
        List<User> users = userRepository.findAll();

        for (User user : users) {
           List<PredictionResult> predictsByUserTemp =  predictionResultRepository.findByUserId(user.getUserName());
           if (!predictsByUserTemp.isEmpty()) {
               TotalPointsDto tmpP = new TotalPointsDto();
               tmpP.setUsername(user.getUserName());
               tmpP.setPoints(String.valueOf(predictionResultRepository.sumPointsByUserId(user.getUserName())));
               listUsers.add(tmpP);

           } else {
               TotalPointsDto tmp = new TotalPointsDto();
               tmp.setUsername(user.getUserName());
               tmp.setPoints("0");
               listUsers.add(tmp);
           }
        }
        //sorting the position
        listUsers.sort((o1, o2) -> Integer.compare(Integer.parseInt(o2.getPoints()), Integer.parseInt(o1.getPoints())));
        for (int i = 0; i < listUsers.size(); i++) {
            TotalPointsDto tmp = listUsers.get(i);
            tmp.setPosition(String.valueOf(i + 1));
        }
        return listUsers;
    }

    public List<PointsInfoDto> getPointsInfo(String username, String round) {
        //ir buscar os driverpoints para aquela raceId
        //ir buscar a predicion do user
        ArrayList<PointsInfoDto> pointsInfo = new ArrayList<>();

        RaceResult raceResult = raceResultRepository.findByRound(round);

        Prediction prediction = predictRepository.findByUserIdAndRound(username, round);
        List<PredictionResult> predictionResult = predictionResultRepository.findByPredictionId(String.valueOf(prediction.getId()));
        PredictionResult predicTemp = predictionResult.get(0);
        if (prediction != null) {
            List<String> drivers  = List.of(prediction.getFirst(), prediction.getSecond(), prediction.getThird());

            List<DriversPoints> driversPoints = driversPointsRepository.findByDriverInAndRaceId(drivers, String.valueOf(raceResult.getId()));

            for (DriversPoints driversPoints1 : driversPoints) {
                PointsInfoDto tmPoints = new PointsInfoDto();
                tmPoints.setDriver(driversPoints1.getDriver());
                tmPoints.setPoints(driversPoints1.getPoints());
                pointsInfo.add(tmPoints);
            }


            predicTemp.setShowPointsUser(false);
            predictionResultRepository.save(predicTemp);
        }

        return pointsInfo;
    }
}
