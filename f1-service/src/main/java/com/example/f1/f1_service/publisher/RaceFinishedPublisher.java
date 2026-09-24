package com.example.f1.f1_service.publisher;


import com.example.f1.f1_service.contracts.race.RaceFinishedEvent;

public interface RaceFinishedPublisher {

    void publish(RaceFinishedEvent event);
}
