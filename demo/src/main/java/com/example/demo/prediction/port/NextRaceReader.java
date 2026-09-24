package com.example.demo.prediction.port;

import com.example.demo.f1.dto.NextRaceData;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface NextRaceReader {
    NextRaceData findNextRace() throws JsonProcessingException;
}
