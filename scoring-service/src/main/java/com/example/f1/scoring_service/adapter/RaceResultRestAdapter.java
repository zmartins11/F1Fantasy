package com.example.f1.scoring_service.adapter;

import com.example.f1.scoring_service.contracts.race.RaceResultData;
import com.example.f1.scoring_service.contracts.race.RaceResultReader;
import org.springframework.stereotype.Component;

@Component
public class RaceResultRestAdapter implements RaceResultReader {

    @Override
    public RaceResultData findTopByRaceFinishedTrueOrderByRoundDesc() {
        return null;
    }

    @Override
    public RaceResultData findByRound(int round) {
        return null;
    }
}
