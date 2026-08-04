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
import java.util.stream.Stream;

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

    public Prediction savePrediction(PredictionDto dto) {

        Prediction prediction = predictRepository
                .findByUserIdAndRound(dto.getUser(), dto.getRound())
                .orElseGet(() -> createNewPrediction(dto));

        if (dto.getFirst() != null) {
            prediction.setFirst(dto.getFirst());
            prediction.setSecond(dto.getSecond());
            prediction.setThird(dto.getThird());
            prediction.setPredictedPodium(true);
        }

        if (dto.getFastestLap() != null) {
            prediction.setFastestLap(dto.getFastestLap());
            prediction.setPredictedFastestLap(true);
        }

        return predictRepository.save(prediction);
    }


    private Prediction createNewPrediction(PredictionDto dto) {

        Prediction prediction = new Prediction();

        prediction.setUserId(dto.getUser());
        prediction.setRound(dto.getRound());

        prediction.setPredictedPodium(false);
        prediction.setPredictedFastestLap(false);

        return prediction;
    }


    // método chamado quando o RaceResult com o race_id da prediction é preenchido
    public int calculate(Prediction prediction, RaceResult raceResult) throws JsonProcessingException {
        int points = 0;
        HashMap<Integer, Integer> resultsPercentage = new HashMap<>();
        int racesCurrentSeason = getSeasonRaces(SEASON_2023);

        if (prediction.getPredictedPodium()) {
            if ((!prediction.getFirst().equals(raceResult.getFirst())) &&
                    (!prediction.getSecond().equals(raceResult.getSecond())) &&
                    (!prediction.getThird().equals(raceResult.getThird())) && (!prediction.getFastestLap().equals(raceResult.getFastestLap()))) {

                createDriversPoints(prediction.getFirst() ,raceResult.getId(), points, "1");
                createDriversPoints(prediction.getSecond(),raceResult.getId(), points, "2");
                createDriversPoints(prediction.getThird(), raceResult.getId(), points, "3");

            }

            points = processDriver("1", raceResult.getId(), prediction.getFirst(), raceResult.getFirst(), points, racesCurrentSeason);
            points = processDriver("2", raceResult.getId(),prediction.getSecond(), raceResult.getSecond(), points, racesCurrentSeason);
            points = processDriver("3", raceResult.getId(), prediction.getThird(), raceResult.getThird(), points, racesCurrentSeason);

        }
        if (prediction.getPredictedFastestLap()) {
            if (prediction.getFastestLap().equals(raceResult.getFastestLap())) {
                int poinsToSave = 0;
                points += 5;
                poinsToSave = 5;
                //createDriversPoints(predictedDriver,raceId, 1, position);
                createDriversPoints(prediction.getFastestLap(), raceResult.getId(),poinsToSave,"fastestLap");
            } else {
                createDriversPoints(prediction.getFastestLap(), raceResult.getId(), 0, "fastestLap");
            }
        }

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
                    createDriversPoints(predictedDriver,raceId, 1, position);
                } else if (percentage >= 50) {
                    points += 3;
                    createDriversPoints(predictedDriver,raceId, 3, position);
                } else if (percentage >= 25) {
                    points += 5;
                    createDriversPoints(predictedDriver,raceId, 5, position);
                } else {
                    points += 10;
                    createDriversPoints(predictedDriver,raceId, 10, position);
                }
            }
        } else {
            createDriversPoints(predictedDriver,raceId,0, position);
        }
        return points;
    }

    private void createDriversPoints(String driver, Integer raceResultId, int points, String position) {
        DriversPoints driversPoints = new DriversPoints();
         if (driversPointsRepository.findByRaceIdAndDriverAndPosition(String.valueOf(raceResultId), driver, position).isEmpty()) {
             driversPoints.setDriver(driver);
             driversPoints.setRaceId(raceResultId);
             driversPoints.setPoints(points);
             driversPoints.setPosition(position);
             driversPointsRepository.save(driversPoints);
         };
    }


    public int getSeasonRaces(String season) {
        List<RaceResult> racesBySeason = raceResultRepository.findBySeason(season);
        return racesBySeason.size();
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
        predictRepository.findByUserIdAndRound(username, nextRaceInfoDto.getRound())
                .ifPresentOrElse(userPrediction -> {

                    nextRaceInfoDto.setUserHavePrediction(true);
                    nextRaceInfoDto.setFirst(userPrediction.getFirst());
                    nextRaceInfoDto.setSecond(userPrediction.getSecond());
                    nextRaceInfoDto.setThird(userPrediction.getThird());

                    if (Boolean.TRUE.equals(userPrediction.getPredictedFastestLap())) {
                        nextRaceInfoDto.setFastestLap(userPrediction.getFastestLap());
                    }
                    nextRaceInfoDto.setPredictedPodium(userPrediction.getPredictedPodium());
                    nextRaceInfoDto.setPredictedFastestLap(userPrediction.getPredictedFastestLap());

                }, () -> nextRaceInfoDto.setUserHavePrediction(false));
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

        RaceResult raceResult = raceResultRepository.findByRound(round);

        if (raceResult == null) {
            return Collections.emptyList();
        }

        Prediction prediction = predictRepository
                .findByUserIdAndRound(username, round)
                .orElseThrow(() -> new RuntimeException("Prediction not found"));

        List<PredictionResult> predictionResults =
                predictionResultRepository.findByPredictionId(String.valueOf(prediction.getId()));

        if (!predictionResults.isEmpty()) {
            PredictionResult predictionResult = predictionResults.get(0);
            predictionResult.setShowPointsUser(false);
            predictionResultRepository.save(predictionResult);
        }

        List<String> drivers = Stream.of(
                        prediction.getFirst(),
                        prediction.getSecond(),
                        prediction.getThird(),
                        prediction.getFastestLap())
                .filter(Objects::nonNull)
                .toList();

        List<DriversPoints> driversPoints =
                driversPointsRepository.findByDriverInAndRaceId(
                        drivers,
                        String.valueOf(raceResult.getId()));

        return driversPoints.stream()
                .map(driver -> {
                    PointsInfoDto dto = new PointsInfoDto();
                    dto.setDriver(driver.getDriver());
                    dto.setPoints(driver.getPoints());
                    dto.setPosition(driver.getPosition());
                    return dto;
                })
                .toList();
    }
}
