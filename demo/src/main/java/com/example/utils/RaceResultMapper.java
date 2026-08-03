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
        raceResultDto.setFirst(results.get(0).getDriver().getFamilyName());
        raceResultDto.setSecond(results.get(1).getDriver().getFamilyName());
        raceResultDto.setThird(results.get(2).getDriver().getFamilyName());
        raceResultDto.setFastestLap(fastestLap.getDriver().getFamilyName());
        return raceResultDto;
    }

}
