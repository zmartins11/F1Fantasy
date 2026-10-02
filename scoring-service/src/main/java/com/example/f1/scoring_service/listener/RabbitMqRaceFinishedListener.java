package com.example.f1.scoring_service.listener;

import com.example.f1.scoring_service.config.RabbitMQConfig;
import com.example.f1.scoring_service.contracts.race.RaceFinishedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "messaging.provider",
        havingValue = "rabbitmq",
        matchIfMissing = true
)
@Slf4j
public class RabbitMqRaceFinishedListener {

    private final RaceFinishedConsumer raceFinishedConsumer;

    public RabbitMqRaceFinishedListener(RaceFinishedConsumer raceFinishedConsumer) {
        this.raceFinishedConsumer = raceFinishedConsumer;
    }

    @RabbitListener(queues = RabbitMQConfig.RACE_FINISHED_QUEUE)
    public void onRaceFinished(RaceFinishedEvent event) {
        log.info("Received race finished event from Pub/Sub: {}", event);
        raceFinishedConsumer.consume(event);
    }
}
