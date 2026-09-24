package com.example.f1.predict_service.contracts.race;


import com.fasterxml.jackson.core.JsonProcessingException;

public interface NextRaceReader {
    NextRaceData findNextRace() throws JsonProcessingException;
}
