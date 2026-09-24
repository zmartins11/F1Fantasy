package com.example.f1.f1_service.publisher;

import com.example.f1.f1_service.config.RabbitMQConfig;
import com.example.f1.f1_service.contracts.race.RaceFinishedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringRaceFinishedPublisher implements RaceFinishedPublisher {

    private final RabbitTemplate rabbitTemplate;

    public SpringRaceFinishedPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(RaceFinishedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RACE_EXCHANGE,
                RabbitMQConfig.RACE_FINISHED_ROUTING_KEY,
                event);
    }
}
