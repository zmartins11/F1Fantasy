package com.example.f1.scoring_service.dto;

public record ScoringHistoryDto( String predictionId,
                                 Integer season,
                                 Integer round,
                                 Integer points) {
}
