package com.example.f1.f1_service.controller;

import com.example.f1.f1_service.contracts.race.RaceResultData;
import com.example.f1.f1_service.dto.NextRaceData;
import com.example.f1.f1_service.model.Driver;
import com.example.f1.f1_service.service.ErgastService;
import com.example.f1.f1_service.service.RaceResultService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/f1")
public class F1InternalController {

    private final ErgastService ergastService;

    private final RaceResultService raceResultService;

    public F1InternalController(ErgastService ergastService, RaceResultService raceResultService) {
        this.ergastService = ergastService;
        this.raceResultService = raceResultService;
    }

    @GetMapping
    public NextRaceData findBySeasonAndRound() throws JsonProcessingException {
        return ergastService.getNextRaceInfo();
    }

    @GetMapping("/drivers/{season}")
    public List<Driver> rawDataDrivers(@PathVariable String season) throws JsonProcessingException {
        return ergastService.getDriversInSeason(season);
    }

    @GetMapping("/latestRaceFinished")
    public RaceResultData latestFinishedRace() throws JsonProcessingException {
        return raceResultService.findTopByRaceFinishedTrueOrderByRoundDesc();
    }

    @GetMapping("/{raceResultId}/pointsCalculated")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markPointsCalculated(@PathVariable Integer raceResultId) throws JsonProcessingException {
        raceResultService.markPointsCalculated(raceResultId);
    }
}
