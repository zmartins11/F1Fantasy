package com.example.f1.scoring_service.contracts.race;


public interface RaceResultReader {

    RaceResultData latestRaceFinished();

    RaceResultData findByRound(int round);
}
