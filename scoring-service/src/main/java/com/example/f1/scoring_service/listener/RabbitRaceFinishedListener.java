package com.example.f1.scoring_service.listener;

import com.example.f1.scoring_service.config.RabbitMQConfig;
import com.example.f1.scoring_service.contracts.race.RaceFinishedEvent;
import com.example.f1.scoring_service.service.ScoringService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitRaceFinishedListener {

    private final ScoringService scoringService;

        public RabbitRaceFinishedListener(ScoringService scoringService) {
        this.scoringService = scoringService;
    }

    @RabbitListener(queues = RabbitMQConfig.RACE_FINISHED_QUEUE)
    public void receive(RaceFinishedEvent event) {
        scoringService.calculateAndSavePoints(event);
    }

}
