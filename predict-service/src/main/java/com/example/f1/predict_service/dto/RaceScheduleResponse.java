package com.example.f1.predict_service.dto;


import com.example.f1.predict_service.contracts.race.NextRaceData;

public record RaceScheduleResponse(
        NextRaceData race,
        UserPredictionData prediction
) {
}
