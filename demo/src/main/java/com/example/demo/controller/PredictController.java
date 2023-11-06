package com.example.demo.controller;

import com.example.demo.model.fantasy.Prediction;
import com.example.demo.model.fantasy.RaceResult;
import com.example.demo.service.ErgastService;
import com.example.demo.service.PredictService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.HashMap;

import static org.apache.coyote.http11.Constants.a;

@RestController
public class PredictController {

    @Autowired
    PredictService predictService;

    @PostMapping("/predict")
    private void savePrediction(@RequestParam String round, @RequestBody Prediction prediction) throws Exception {
        //season static value
        Year currentYear = Year.now();
        String season = String.valueOf(currentYear.getValue());
        //check if raceFinished
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

    @GetMapping("/test")
    private String testApi() throws JsonProcessingException {
        return "logged in success inside app";
    }
}
