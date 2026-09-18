package com.example.demo.scoring.port;

import com.example.demo.f1.model.RaceResult;

public interface RaceResultReader {
    RaceResultData findBySeasonAndRound(Integer season, Integer round);

    RaceResultData findTopByRaceFinishedTrueOrderByRoundDesc();

    RaceResultData findByRound(int round);
}
