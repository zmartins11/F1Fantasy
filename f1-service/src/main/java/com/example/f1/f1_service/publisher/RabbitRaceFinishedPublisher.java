package com.example.f1.f1_service.publisher;

import com.example.f1.f1_service.config.RabbitMQConfig;
import com.example.f1.f1_service.contracts.race.RaceFinishedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "messaging.provider",
        havingValue = "rabbitmq",
        matchIfMissing = true
)
@Slf4j
public class RabbitRaceFinishedPublisher implements RaceFinishedPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitRaceFinishedPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(RaceFinishedEvent event) {
        log.debug("Publishing race finished event: {}", event);
        rabbitTemplate.convertAndSend(RabbitMQConfig.RACE_FINISHED_EXCHANGE, RabbitMQConfig.RACE_FINISHED_ROUTING_KEY, event);
    }
}
