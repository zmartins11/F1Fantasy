package com.example.demo.f1.publisher;

import com.example.demo.contracts.race.RaceFinishedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringRaceFinishedPublisher implements RaceFinishedPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringRaceFinishedPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(RaceFinishedEvent event) {
        publisher.publishEvent(event);
    }
}
