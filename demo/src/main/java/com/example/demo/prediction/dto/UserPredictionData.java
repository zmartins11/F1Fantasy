package com.example.demo.prediction.dto;

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
