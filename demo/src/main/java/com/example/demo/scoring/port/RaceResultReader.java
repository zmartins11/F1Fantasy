package com.example.demo.scoring.port;

public interface RaceResultReader {
    RaceResultData findBySeasonAndRound(Integer season, Integer round);
}
