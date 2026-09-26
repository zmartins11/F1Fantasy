package com.example.f1.f1_service.contracts.race;

public record RaceResultData(
        Integer id,
        Integer season,
        Integer round,
        String first,
        String second,
        String third,
        String fastestLap
) {
}
