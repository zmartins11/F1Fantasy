package com.example.demo.scoring.port;

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
