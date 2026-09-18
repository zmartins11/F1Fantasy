package com.example.demo.prediction.controller;

import com.example.demo.prediction.dto.NextRaceInfoDto;
import com.example.demo.prediction.dto.PredictionDto;
import com.example.demo.prediction.model.Prediction;
import com.example.demo.f1.service.ErgastService;
import com.example.demo.prediction.service.PredictService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import org.springframework.http.ResponseEntity;

@RestController
public class PredictController {

    private final PredictService predictService;
    private final ErgastService ergastService;

    public PredictController(PredictService predictService, ErgastService ergastService) {
        this.predictService = predictService;
        this.ergastService = ergastService;
    }

    @PostMapping("/predict")
    public ResponseEntity<PredictionDto> savePrediction(@RequestBody PredictionDto prediction) {
        Prediction savedPrediction = predictService.savePrediction(prediction);
        prediction.setPredictedPodium(savedPrediction.getPredictedPodium());
        prediction.setPredictedFastestLap(savedPrediction.getPredictedFastestLap());
        prediction.setFirst(savedPrediction.getFirst());
        prediction.setSecond(savedPrediction.getSecond());
        prediction.setThird(savedPrediction.getThird());
        prediction.setFastestLap(savedPrediction.getFastestLap());
        prediction.setUserId(savedPrediction.getUserId());
        return ResponseEntity.ok(prediction);
    }


    @GetMapping("/raceSchedule")
    public ResponseEntity<NextRaceInfoDto> getNextRaceInfo(Authentication authentication) throws JsonProcessingException {
        //TimeUnit.SECONDS.sleep(3);
        //RaceResult nextRaceInfo = predictService.getNextRaceInfo();
        NextRaceInfoDto nextRaceInfoDto = ergastService.getNextRaceInfo();


        //checkUserPredictions
        Integer userId = predictService.getAuthenticatedUserId(authentication);
        nextRaceInfoDto = predictService.getUserPrediction(nextRaceInfoDto, userId);

        return ResponseEntity.ok(nextRaceInfoDto);
    }

}
