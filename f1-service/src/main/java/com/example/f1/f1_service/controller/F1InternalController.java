package com.example.f1.f1_service.controller;

import com.example.f1.f1_service.contracts.race.RaceResultData;
import com.example.f1.f1_service.dto.NextRaceData;
import com.example.f1.f1_service.model.Driver;
import com.example.f1.f1_service.service.ErgastService;
import com.example.f1.f1_service.service.RaceResultProcessingService;
import com.example.f1.f1_service.service.RaceResultService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/f1")
@Slf4j
public class F1InternalController {

    private final ErgastService ergastService;

    private final RaceResultService raceResultService;

    private final RaceResultProcessingService raceResultProcessingService;

    public F1InternalController(ErgastService ergastService, RaceResultService raceResultService, RaceResultProcessingService raceResultProcessingService) {
        this.ergastService = ergastService;
        this.raceResultService = raceResultService;
        this.raceResultProcessingService = raceResultProcessingService;
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

    @PostMapping("/api/f1/admin/process-latest-race")
    public ResponseEntity<Void> processLatestRace() throws JsonProcessingException {
        log.info("PROCESS LATEST RACE API CALLED........");
        raceResultProcessingService.processLatestFinishedRace();
        return ResponseEntity.ok().build();
    }
}
