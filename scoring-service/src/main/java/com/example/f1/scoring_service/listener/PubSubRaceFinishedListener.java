package com.example.f1.scoring_service.listener;

import com.example.f1.scoring_service.contracts.race.RaceFinishedEvent;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@ConditionalOnProperty(
        name = "messaging.provider",
        havingValue = "pubsub"
)
@Slf4j
public class PubSubRaceFinishedListener {

    private static final String SUBSCRIPTION = "race-finished-scoring-sub";

    private final PubSubTemplate pubSubTemplate;
    private final RaceFinishedConsumer raceFinishedConsumer;
    private final ObjectMapper objectMapper;

    public PubSubRaceFinishedListener(PubSubTemplate pubSubTemplate, RaceFinishedConsumer raceFinishedConsumer, ObjectMapper objectMapper) {
        this.pubSubTemplate = pubSubTemplate;
        this.raceFinishedConsumer = raceFinishedConsumer;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void subscribe() {
        log.info("Starting Pub/Sub listener for subscription: {}", SUBSCRIPTION);
        pubSubTemplate.subscribe(SUBSCRIPTION, message -> {
            try {

                RaceFinishedEvent event = objectMapper.readValue(
                        message.getPubsubMessage().getData().toByteArray(),
                        RaceFinishedEvent.class
                );

                log.info("Received race finished event from Pub/Sub: {}", event);

                raceFinishedConsumer.consume(event);

                log.info("RaceFinishedEvent processed successfully: {}", event);

                message.ack();

            } catch (Exception e) {
                log.error("Failed to process race finished event from Pub/Sub", e);
                message.nack();
            }
        });
    }
}

