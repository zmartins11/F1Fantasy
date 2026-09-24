package com.example.demo.contracts.race;

public record RaceFinishedEvent(
        Integer raceResultId,
        Integer season,
        Integer round,
        String first,
        String second,
        String third,
        String fastestLap) {
}
