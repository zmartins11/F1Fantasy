package com.example.f1.scoring_service.contracts.race;

public record RaceFinishedEvent(
        Integer raceResultId,
        Integer season,
        Integer round,
        String first,
        String second,
        String third,
        String fastestLap) {
}
