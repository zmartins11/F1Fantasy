package com.example.f1.f1_service.utils;


import com.example.f1.f1_service.enums.DriverDisplayType;
import com.example.f1.f1_service.model.RaceResultDto;
import com.example.f1.f1_service.model.Results;

import java.util.List;

public class RaceResultMapper {
    public RaceResultDto map(List<Results> results, Results fastestLap, String round, String season, DriverDisplayType displayType) {
        RaceResultDto raceResultDto = new RaceResultDto();

        raceResultDto.setRound(Integer.parseInt(round));
        raceResultDto.setSeason(season);
        raceResultDto.setFirst(getDriverValue(results.get(0), displayType));
        raceResultDto.setSecond(getDriverValue(results.get(1), displayType));
        raceResultDto.setThird(getDriverValue(results.get(2), displayType));
        raceResultDto.setFastestLap(getDriverValue(fastestLap, displayType));
        return raceResultDto;
    }

    private String getDriverValue(Results result, DriverDisplayType displayType) {
        return switch (displayType) {
            case FAMILY_NAME -> result.getDriver().getFamilyName();
            case NUMBER -> String.valueOf(result.getDriver().getPermanentNumber());
        };
    }

}
