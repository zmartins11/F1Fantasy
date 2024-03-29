package com.example.utils;

import com.example.demo.model.Results;
import com.example.demo.model.fantasy.RaceResult;

import java.util.List;

public class RaceResultMapper {
    public RaceResult map(List<Results> results, Results fastestLap, String round, String season) {
        RaceResult raceResult = new RaceResult();
        raceResult.setRound(Integer.parseInt(round));
        raceResult.setSeason(season);
        raceResult.setFirst(results.get(0).getNumber());
        raceResult.setSecond(results.get(1).getNumber());
        raceResult.setThird(results.get(2).getNumber());
        raceResult.setFastestLap(fastestLap.getNumber());
        return raceResult;
    }

}
