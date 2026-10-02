package com.example.f1.scoring_service.listener;

import com.example.f1.scoring_service.contracts.race.RaceFinishedEvent;
import com.example.f1.scoring_service.service.ScoringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RaceFinishedEventHandler  implements RaceFinishedConsumer {

    private final ScoringService scoringService;

    public RaceFinishedEventHandler(ScoringService scoringService) {
        this.scoringService = scoringService;
    }
    @Override
    public void consume(RaceFinishedEvent event) {
        log.info("Starting scoring for race: {}", event.raceResultId());
        scoringService.calculateAndSavePoints(event);
        log.info("Scoring completed for race: {}", event.raceResultId());
    }
}
