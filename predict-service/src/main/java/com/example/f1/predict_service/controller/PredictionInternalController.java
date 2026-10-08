package com.example.f1.predict_service.controller;

import com.example.f1.predict_service.contracts.prediction.PredictionData;
import com.example.f1.predict_service.service.PredictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/predictions")
public class PredictionInternalController {

    private final PredictService predictService;

    public PredictionInternalController(PredictService predictService) {
        this.predictService = predictService;
    }

    @GetMapping
    public List<PredictionData> findBySeasonAndRound(
            @RequestParam Integer season,
            @RequestParam Integer round) {

        return predictService.findBySeasonAndRound(season, round);
    }

    @GetMapping("/user")
    public ResponseEntity<PredictionData> findByUserIdAndRound(
            @RequestParam Integer userId,
            @RequestParam Integer round) {

        return predictService.findByUserIdAndRound(userId, round)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
