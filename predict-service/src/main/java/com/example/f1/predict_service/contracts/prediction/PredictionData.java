package com.example.f1.predict_service.contracts.prediction;

public record PredictionData(
        Integer id,
        Integer userId,
        Integer season,
        Integer round,
        String first,
        String second,
        String third,
        String fastestLap,
        Boolean predictedPodium,
        Boolean predictedFastestLap
) {
}
