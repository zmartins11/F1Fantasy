package com.example.demo.scoring.adapter;

import com.example.demo.f1.model.RaceResult;
import com.example.demo.f1.service.RaceResultService;
import com.example.demo.scoring.port.RaceResultData;
import com.example.demo.scoring.port.RaceResultReader;
import com.example.demo.scoring.port.RaceResultWriter;
import org.springframework.stereotype.Component;

@Component
public class RaceResultJpaAdapter implements RaceResultReader, RaceResultWriter {

    private final RaceResultService raceResultService;

    public RaceResultJpaAdapter(RaceResultService raceResultService) {
        this.raceResultService = raceResultService;
    }

    @Override
    public RaceResultData findBySeasonAndRound(Integer season, Integer round) {
        RaceResult raceResult = raceResultService.findBySeasonAndRound(season, round);
        if (raceResult == null) {
            return null;
        }
        return toData(raceResult);
    }

    @Override
    public RaceResultData findTopByRaceFinishedTrueOrderByRoundDesc() {
        return toData(raceResultService.findTopByRaceFinishedTrueOrderByRoundDesc());
    }

    @Override
    public RaceResultData findByRound(int round) {
        return toData(raceResultService.findByRound(round));
    }

    @Override
    public void markPointsCalculated(Integer raceResultId) {
        RaceResult raceResult = raceResultService.findById(raceResultId);
        if (raceResult == null) {
            throw new IllegalStateException("Race result not found: " + raceResultId);
        }

        raceResult.setPointsCalculated(true);
        raceResultService.save(raceResult);
    }

    private RaceResultData toData(RaceResult raceResult) {
        if (raceResult == null) {
            return null;
        }

        return new RaceResultData(raceResult.getId(),
                raceResult.getSeason(),
                raceResult.getRound(),
                raceResult.getFirst(),
                raceResult.getSecond(),
                raceResult.getThird(),
                raceResult.getFastestLap());
    }
}
