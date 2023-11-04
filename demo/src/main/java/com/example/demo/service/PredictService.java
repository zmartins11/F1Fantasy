package com.example.demo.service;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public void savePrediction(Prediction prediction) {
        predictRepository.save(prediction);
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
}
