package com.example.demo.scoring.listener;

import com.example.demo.contracts.race.RaceFinishedEvent;
import com.example.demo.scoring.service.ScoringService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RaceFinishedListener {

    private final ScoringService scoringService;

    public RaceFinishedListener(ScoringService scoringService) {
        this.scoringService = scoringService;
    }

    @EventListener
    public void handle(RaceFinishedEvent event) {
        scoringService.calculateAndSavePoints(event);
    }
}
