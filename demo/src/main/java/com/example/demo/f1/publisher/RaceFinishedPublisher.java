package com.example.demo.f1.publisher;

import com.example.demo.contracts.race.RaceFinishedEvent;

public interface RaceFinishedPublisher {

    void publish(RaceFinishedEvent event);
}
