package com.example.demo.scoring.port;

import com.example.demo.contracts.race.RaceResultData;

public interface RaceResultReader {
    RaceResultData findBySeasonAndRound(Integer season, Integer round);

    RaceResultData findTopByRaceFinishedTrueOrderByRoundDesc();

    RaceResultData findByRound(int round);
}
