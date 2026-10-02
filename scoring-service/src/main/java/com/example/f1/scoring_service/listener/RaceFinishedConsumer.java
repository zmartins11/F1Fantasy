package com.example.f1.scoring_service.listener;

import com.example.f1.scoring_service.contracts.race.RaceFinishedEvent;

public interface RaceFinishedConsumer {
    void consume(RaceFinishedEvent event);
}
