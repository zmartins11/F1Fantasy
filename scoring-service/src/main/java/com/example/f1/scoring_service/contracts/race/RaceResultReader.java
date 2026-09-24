package com.example.f1.scoring_service.contracts.race;


import com.example.f1.scoring_service.contracts.race.RaceResultData;

public interface RaceResultReader {

    RaceResultData findTopByRaceFinishedTrueOrderByRoundDesc();

    RaceResultData findByRound(int round);
}
