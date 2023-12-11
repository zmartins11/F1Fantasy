package com.example.cronJob;

import com.example.demo.model.RaceResponse;
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
import java.util.Optional;

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
        boolean sunday = true;
        try {
            RaceResult currentRace = raceResultRepository.findTopByRaceFinishedFalseOrderByRoundAsc();
            RaceResult raceResultTemp = ergastService.getRaceResult(currentRace.getSeason(), currentRace.getRound());
            if (raceResultTemp != null) {
                //TODO : comentar para testes
                currentRace.setRaceFinished(true);
                currentRace.setFirst(raceResultTemp.getFirst());
                currentRace.setSecond(raceResultTemp.getSecond());
                currentRace.setThird(raceResultTemp.getThird());
                raceResultRepository.save(currentRace);

                // check if there are predictions for the raceResult added :
                List<Prediction> listPredictions = predictRepository.findByRaceId(String.valueOf(currentRace.getId()));
                if (!listPredictions.isEmpty()) {
                    for (Prediction prediction : listPredictions) {
                        if (predictionResultRepository.findByPredictionId(String.valueOf(prediction.getId())).isEmpty()) {
                            int points = predictService.calculate(prediction, currentRace);
                            PredictionResult predictionResult = new PredictionResult();
                            predictionResult.setPredictionId(String.valueOf(prediction.getId()));
                            predictionResult.setPoints(points);
                            predictionResult.setRaceId(String.valueOf(currentRace.getId()));
                            predictionResult.setUserId(prediction.getUserId());
                            predictionResultRepository.save(predictionResult);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Round -  not finished!");
        }
    }
}
