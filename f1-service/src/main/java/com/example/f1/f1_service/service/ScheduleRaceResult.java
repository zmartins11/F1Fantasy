package com.example.f1.f1_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleRaceResult {

    private final RaceResultProcessingService raceResultProcessingService;

    public ScheduleRaceResult(RaceResultProcessingService raceResultProcessingService) {
        this.raceResultProcessingService = raceResultProcessingService;
    }

    //@Scheduled(fixedRate = 30 * 60 * 1000)
    public void populateRaceResult() throws JsonProcessingException {
        raceResultProcessingService.processLatestFinishedRace();
    }

}
