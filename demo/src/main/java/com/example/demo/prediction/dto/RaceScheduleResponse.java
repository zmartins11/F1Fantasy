package com.example.demo.prediction.dto;

import com.example.demo.f1.dto.NextRaceData;

public record RaceScheduleResponse(
        NextRaceData race,
        UserPredictionData prediction
) {
}
