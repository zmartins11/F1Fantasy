package com.example.f1.predict_service.controller;

import com.example.f1.predict_service.dto.PredictionDto;
import com.example.f1.predict_service.dto.RaceScheduleResponse;
import com.example.f1.predict_service.model.Prediction;
import com.example.f1.predict_service.service.PredictService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/predictions")
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
    public ResponseEntity<RaceScheduleResponse> getNextRaceInfo(@AuthenticationPrincipal Jwt jwt) throws JsonProcessingException {
        return ResponseEntity.ok(predictService.getRaceSchedule(jwt.getClaim("userId")));
    }

}
