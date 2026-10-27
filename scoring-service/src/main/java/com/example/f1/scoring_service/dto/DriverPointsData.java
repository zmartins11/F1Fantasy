package com.example.f1.scoring_service.dto;

public record DriverPointsData( Integer predictionId,
                                String driver,
                                Integer raceResultId,
                                int points,
                                String position) {
}
