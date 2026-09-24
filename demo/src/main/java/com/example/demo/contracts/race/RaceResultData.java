package com.example.demo.contracts.race;

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
