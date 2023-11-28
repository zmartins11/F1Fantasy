package com.example.demo.service;

import com.example.demo.dto.NextRaceInfoDto;
import com.example.demo.dto.PredictionDto;
import com.example.demo.dto.TotalPointsDto;
import com.example.demo.enums.Formula1DriverEnum;
import com.example.demo.model.fantasy.Prediction;
import com.example.demo.model.fantasy.PredictionResult;
import com.example.demo.model.fantasy.RaceResult;
import com.example.demo.repository.PredictRepository;
import com.example.demo.repository.PredictionResultRepository;
import com.example.demo.repository.RaceResultRepository;
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

    private final static String SEASON_2023 = "2023";
    private final static String SEASON_2022 = "2022";

    public RaceResult findBySeasonAndRound(String season, String round) {
        return raceResultRepository.findBySeasonAndRound(season, round);
    }


    public void saveRace(RaceResult raceResult) {

        RaceResult existingRaceResult = raceResultRepository.findBySeasonAndRound(raceResult.getSeason(), raceResult.getRound());
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
        Prediction predictionSaved = predictRepository.findByUserIdAndRound(prediction.getUser(), prediction.getRound());
        if (predictionSaved != null) {
            predictionSaved.setFirst(prediction.getFirst());
            predictionSaved.setSecond(prediction.getSecond());
            predictionSaved.setThird(prediction.getThird());
            return predictRepository.save(predictionSaved);
        } else {
            Prediction newPrediction = new Prediction();
            newPrediction.setFirst(prediction.getFirst());
            newPrediction.setSecond(prediction.getSecond());
            newPrediction.setThird(prediction.getThird());
            newPrediction.setUserId(prediction.getUser());
            newPrediction.setRound(prediction.getRound());
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
            return points;
        }

        if (prediction.getFirst().equals(raceResult.getFirst())) {
            resultsPercentage = ergastService.testApiGetResult("1", racesCurrentSeason,Formula1DriverEnum.getNameByNumber(Integer.parseInt(raceResult.getFirst())));
            Iterator<Map.Entry<Integer, Integer>> iterator = resultsPercentage.entrySet().iterator();
            if (iterator.hasNext()) {
                Map.Entry<Integer, Integer> firstEntry = iterator.next();
                int percentageFirst = (int)((double)firstEntry.getKey() / firstEntry.getValue() * 100);
                if (percentageFirst >= 75) {
                    points += 1;
                } else if (percentageFirst >= 50) {
                    points += 3;
                } else if (percentageFirst >= 25) {
                    points += 5;
                } else {
                    points += 10;
                }
            }
        }
        if (prediction.getSecond().equals(raceResult.getSecond())) {
            resultsPercentage = ergastService.testApiGetResult("2", racesCurrentSeason,Formula1DriverEnum.getNameByNumber(Integer.parseInt(raceResult.getSecond())));
            Iterator<Map.Entry<Integer, Integer>> iterator = resultsPercentage.entrySet().iterator();
            if (iterator.hasNext()) {
                Map.Entry<Integer, Integer> firstEntry = iterator.next();
                int percentaSecond = (int)((double)firstEntry.getKey() / firstEntry.getValue() * 100);
                if (percentaSecond >= 75) {
                    points += 1;
                } else if (percentaSecond >= 50) {
                    points += 3;
                } else if (percentaSecond >= 25) {
                    points += 5;
                } else {
                    points += 10;
                }
            }
        }
        if (prediction.getThird().equals(raceResult.getThird())) {
            resultsPercentage = ergastService.testApiGetResult("3", racesCurrentSeason,Formula1DriverEnum.getNameByNumber(Integer.parseInt(raceResult.getThird())));
            Iterator<Map.Entry<Integer, Integer>> iterator = resultsPercentage.entrySet().iterator();
            if (iterator.hasNext()) {
                Map.Entry<Integer, Integer> firstEntry = iterator.next();
                int percentageThird = (int)((double)firstEntry.getKey() / firstEntry.getValue() * 100);
                if (percentageThird >= 75) {
                    points += 1;
                } else if (percentageThird >= 50) {
                    points += 3;
                } else if (percentageThird >= 25) {
                    points += 5;
                } else {
                    points += 10;
                }
            }
        }
        return points;
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

    public Optional<RaceResult> getNextRaceInfo() {
        return raceResultRepository.findTopByRaceFinishedFalseOrderByRoundAsc();
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
        } else {
            nextRaceInfoDto.setUserHavePrediction(Boolean.FALSE);
        }
        return nextRaceInfoDto;
    }

    public TotalPointsDto getTotalPointsByUser(String username) {
        List<PredictionResult> result = predictionResultRepository.findByUserId(username);
        Long points = predictionResultRepository.sumPointsByUserId(username);
        if (points==null) {
            points = 0L;
        }
        TotalPointsDto totalPointsDto = new TotalPointsDto();
        totalPointsDto.setUsername(username);
        totalPointsDto.setPoints(String.valueOf(points));

        return totalPointsDto;
    }

    public List<TotalPointsDto> getTotalPoints() {
        List<Object[]> result = predictionResultRepository.findTotalPointsByUser();
        List<TotalPointsDto> listUsers = new ArrayList<>();
        int position = 1;
        for (Object obj : result) {
            Object[] array = (Object[]) obj;

            String username = (String) array[0];
            Long points = (Long) array[1];

            TotalPointsDto tmp = new TotalPointsDto();
            tmp.setPosition(String.valueOf(position));
            tmp.setUsername(username);
            tmp.setPoints(String.valueOf(points));
            listUsers.add(tmp);
            position ++;
        }
        return listUsers;
    }
}
