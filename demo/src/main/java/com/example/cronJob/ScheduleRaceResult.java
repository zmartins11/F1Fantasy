package com.example.cronJob;

import com.example.demo.model.RaceResultDto;
import com.example.demo.model.fantasy.Prediction;
import com.example.demo.model.fantasy.PredictionResult;
import com.example.demo.model.fantasy.RaceResult;
import com.example.demo.repository.PredictRepository;
import com.example.demo.repository.PredictionResultRepository;
import com.example.demo.repository.RaceResultRepository;
import com.example.demo.service.ErgastService;
import com.example.demo.service.PredictService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ScheduleRaceResult {

    // task runns every monday / tuesday
    // para correr api : round (vou buscar à tabela race_result : select * from table where finished = false)
    //                  season (vou buscar à tabela)
    // if mrData.getRaces is null (corrida ainda não terminada)
    // deste component passar o resultado para o predictService para guardar results.
    // no fim desta operacao fazer calculo dos pontos

    @Autowired
    private RaceResultRepository raceResultRepository;
    @Autowired
    private ErgastService ergastService;
    @Autowired
    private PredictRepository predictRepository;
    @Autowired
    private PredictService predictService;
    @Autowired
    private PredictionResultRepository predictionResultRepository;


    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void populateRaceResult() throws JsonProcessingException {
        RaceResult currentRace =
                raceResultRepository.findTopByRaceFinishedFalseOrderByRoundAsc();

        if (currentRace == null) {
            return;
        }

        try {

            RaceResultDto apiRaceResult = ergastService.getRaceResult(
                    currentRace.getSeason(),
                    String.valueOf(currentRace.getRound()));

            // ainda não existem resultados
            if (apiRaceResult == null) {
                return;
            }

            updateRaceResult(currentRace, apiRaceResult);

            if (!Boolean.TRUE.equals(currentRace.isPointsCalculated())) {
                calculatePoints(currentRace);
            }

        } catch (Exception e) {
            System.out.println("Race " + currentRace.getRound() + " not finished yet.");
        }
    }

    private void updateRaceResult(RaceResult currentRace, RaceResultDto apiRaceResult) {
        currentRace.setRaceFinished(true);
        currentRace.setFirst(apiRaceResult.getFirst());
        currentRace.setSecond(apiRaceResult.getSecond());
        currentRace.setThird(apiRaceResult.getThird());
        currentRace.setFastestLap(apiRaceResult.getFastestLap());

        raceResultRepository.save(currentRace);
    }

    private void calculatePoints(RaceResult currentRace) throws JsonProcessingException {

        List<Prediction> predictions =
                predictRepository.findBySeasonAndRound(
                        currentRace.getSeason(),
                        currentRace.getRound());

        Map<String, Integer> driverPoints = predictService.buildDriverPoints(currentRace);

        for (Prediction prediction : predictions) {

            int points = predictService.calculate(prediction, currentRace, driverPoints);

            PredictionResult result = new PredictionResult();
            result.setPredictionId(String.valueOf(prediction.getId()));
            result.setUserId(prediction.getUserId());
            result.setSeason(currentRace.getSeason());
            result.setRound(currentRace.getRound());
            result.setPoints(points);
            result.setShowPointsUser(Boolean.TRUE);

            predictionResultRepository.save(result);
        }

        currentRace.setPointsCalculated(true);
        raceResultRepository.save(currentRace);
    }
}
