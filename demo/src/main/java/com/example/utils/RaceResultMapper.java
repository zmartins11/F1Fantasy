package com.example.utils;

import com.example.demo.model.RaceResultDto;
import com.example.demo.model.Results;
import com.example.demo.model.fantasy.RaceResult;

import java.util.List;

public class RaceResultMapper {
    public RaceResultDto map(List<Results> results, Results fastestLap, String round, String season) {
        RaceResultDto raceResultDto = new RaceResultDto();
        raceResultDto.setRound(Integer.parseInt(round));
        raceResultDto.setSeason(season);
        raceResultDto.setFirst(String.valueOf(results.get(0).getDriver().getPermanentNumber()));
        raceResultDto.setSecond(String.valueOf(results.get(1).getDriver().getPermanentNumber()));
        raceResultDto.setThird(String.valueOf(results.get(2).getDriver().getPermanentNumber()));
        raceResultDto.setFastestLap(String.valueOf(fastestLap.getDriver().getPermanentNumber()));
        return raceResultDto;
    }

}
