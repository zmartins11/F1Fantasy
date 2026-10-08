package com.example.f1.predict_service.dto;

public record UserPredictionData(
        Boolean userHavePrediction,
        String first,
        String second,
        String third,
        String fastestLap,
        Boolean predictedPodium,
        Boolean predictedFastestLap
) {
}
