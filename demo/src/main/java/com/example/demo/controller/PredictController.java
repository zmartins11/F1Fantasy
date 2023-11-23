package com.example.demo.controller;

import com.example.demo.dto.NextRaceInfoDto;
import com.example.demo.dto.PredictionDto;
import com.example.demo.exception.SavePredictionException;
import com.example.demo.model.Race;
import com.example.demo.model.fantasy.Prediction;
import com.example.demo.model.fantasy.RaceResult;
import com.example.demo.service.ErgastService;
import com.example.demo.service.PredictService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
public class PredictController {

    @Autowired
    PredictService predictService;
    @Autowired
    private ErgastService ergastService;

    @PostMapping("/predict")
    private PredictionDto savePrediction(@RequestBody PredictionDto prediction) throws Exception {
        //season static value
        Year currentYear = Year.now();
        String season = String.valueOf(currentYear.getValue());
        //check if raceFinished
        //TODO: GET THE ROUND STRING FOR PREDICTION OBJECT
        boolean raceFinished = predictService.checkRaceFinished(season, prediction.getRound());
        if (raceFinished) {
            throw new SavePredictionException("race already finish");
        }
        predictService.savePrediction(prediction);
        return prediction;
    }

    @GetMapping("/predictResult")
    private void getPrediction(@RequestParam String round) throws Exception {
        Year currentYear = Year.now();
        String season = String.valueOf(currentYear.getValue());
        //check if raceFinished
        boolean raceFinished = predictService.checkRaceFinished(season, round);
        if (raceFinished) {
            throw new Exception("race already finish");
        }
        RaceResult raceResult = predictService.getRace(season, round);
        //TODO
    }

    @GetMapping("/raceSchedule")
    private NextRaceInfoDto getRaceInfo(@RequestParam String username) throws JsonProcessingException, InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        Optional<RaceResult> nextRaceInfo = predictService.getNextRaceInfo();
        NextRaceInfoDto nextRaceInfoDto = ergastService.getScheduleRace(nextRaceInfo);

        //houve atualizacao do predictionLocked
        if (!nextRaceInfoDto.getPredictionLocked().equals(nextRaceInfo.get().isPredictionLocked())) {
            nextRaceInfo.get().setPredictionLocked(nextRaceInfoDto.getPredictionLocked());
            predictService.updatePredictionLocked(nextRaceInfoDto);
        }
        //checkUserPredictions
        nextRaceInfoDto = predictService.getUserPrediction(nextRaceInfoDto, username);

        return nextRaceInfoDto;
    }

    @GetMapping("/test")
    private String test() {
        return "teste";
    }



}
