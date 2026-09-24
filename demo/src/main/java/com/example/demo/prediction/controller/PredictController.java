package com.example.demo.prediction.controller;

import com.example.demo.prediction.dto.PredictionDto;
import com.example.demo.prediction.dto.RaceScheduleResponse;
import com.example.demo.prediction.model.Prediction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.demo.prediction.service.PredictService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import org.springframework.http.ResponseEntity;

@RestController
public class PredictController {

    private final PredictService predictService;

    public PredictController(PredictService predictService) {
        this.predictService = predictService;
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
    public ResponseEntity<RaceScheduleResponse> getNextRaceInfo(Authentication authentication) throws JsonProcessingException {
        Integer userId = predictService.getAuthenticatedUserId(authentication);
        return ResponseEntity.ok(predictService.getRaceSchedule(userId));
    }

}
