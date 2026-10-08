package com.example.f1.f1_service.publisher;

import com.example.f1.f1_service.contracts.race.RaceFinishedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
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
public class PubSubRaceFinishedPublisher implements RaceFinishedPublisher{

    private static final String TOPIC = "race-finished";

    private final PubSubTemplate pubSubTemplate;
    private final ObjectMapper objectMapper;

    public PubSubRaceFinishedPublisher(PubSubTemplate pubSubTemplate, ObjectMapper objectMapper) {
        this.pubSubTemplate = pubSubTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(RaceFinishedEvent event) {
        String payload = objectMapper.writeValueAsString(event);
        log.info(".........Publishing race finished event to Pub/Sub: {}", payload);
        pubSubTemplate.publish(TOPIC, payload);
    }
}

