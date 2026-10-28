package com.example.f1.predict_service.controller;

import com.example.f1.predict_service.contracts.prediction.PredictionData;
import com.example.f1.predict_service.dto.PredictionDto;
import com.example.f1.predict_service.dto.PredictionStatsDto;
import com.example.f1.predict_service.dto.RaceScheduleResponse;
import com.example.f1.predict_service.model.Prediction;
import com.example.f1.predict_service.service.PredictService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/predictions")
public class PredictController {

    private final PredictService predictService;

    public PredictController(PredictService predictService) {
        this.predictService = predictService;
    }

    @PostMapping("/predict")
    public ResponseEntity<PredictionDto> savePrediction(
            @RequestBody PredictionDto prediction) {

        PredictionDto savedPrediction =
                predictService.savePrediction(prediction);

        return ResponseEntity.ok(savedPrediction);
    }


    @GetMapping("/raceSchedule")
    public ResponseEntity<RaceScheduleResponse> getNextRaceInfo(@AuthenticationPrincipal Jwt jwt) throws JsonProcessingException {
        return ResponseEntity.ok(predictService.getRaceSchedule(jwt.getClaim("userId")));
    }


    @GetMapping("/me/stats/{season}")
    public ResponseEntity<PredictionStatsDto> getPredictionsStatsBySeason(@PathVariable Integer season, @AuthenticationPrincipal Jwt jwt) throws JsonProcessingException {
        return ResponseEntity.ok(predictService.getPredictionStatsBySeason(jwt.getClaim("userId"), season));
    }

    @GetMapping("/season/{season}/round/{round}")
    public ResponseEntity<PredictionData> getPredictionBySeasonAndRound(
            @PathVariable Integer season,
            @PathVariable Integer round,
            @AuthenticationPrincipal Jwt jwt) {

        Long userId = jwt.getClaim("userId");

        return predictService
                .findByUserIdAndSeasonAndRound(userId.intValue(), season, round)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
