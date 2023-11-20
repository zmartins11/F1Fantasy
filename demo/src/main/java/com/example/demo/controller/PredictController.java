package com.example.demo.controller;

import com.example.demo.dto.NextRaceInfoDto;
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
    private void savePrediction(@RequestParam String round, @RequestBody Prediction prediction) throws Exception {
        //season static value
        Year currentYear = Year.now();
        String season = String.valueOf(currentYear.getValue());
        //check if raceFinished
        //TODO: GET THE ROUND STRING FOR PREDICTION OBJECT
        boolean raceFinished = predictService.checkRaceFinished(season, round);
        if (raceFinished) {
            throw new Exception("race already finish");
        }
        RaceResult raceResult = predictService.getRace(season, round);
        prediction.setRaceId(String.valueOf(raceResult.getId()));
        predictService.savePrediction(prediction);
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
    private NextRaceInfoDto getRaceInfo() throws JsonProcessingException, InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        Optional<RaceResult> nextRaceInfo = predictService.getNextRaceInfo();
        NextRaceInfoDto nextRaceInfoDto = ergastService.getScheduleRace(nextRaceInfo);

        //houve atualizacao do predictionLocked
        if (!nextRaceInfoDto.getPredictionLocked().equals(nextRaceInfo.get().isPredictionLocked())) {
            nextRaceInfo.get().setPredictionLocked(nextRaceInfoDto.getPredictionLocked());
            predictService.updatePredictionLocked(nextRaceInfoDto);
        }
        return nextRaceInfoDto;
    }

    @GetMapping("/test")
    private String test() {
        return "teste";
    }



}
