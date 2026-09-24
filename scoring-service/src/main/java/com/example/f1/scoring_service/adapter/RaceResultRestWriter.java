package com.example.f1.scoring_service.adapter;

import com.example.f1.scoring_service.contracts.race.RaceResultWriter;
import org.springframework.stereotype.Component;

@Component
public class RaceResultRestWriter implements RaceResultWriter {
    @Override
    public void markPointsCalculated(Integer raceResultId) {

    }
}
