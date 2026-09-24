package com.example.demo.prediction.adapter;

import com.example.demo.f1.dto.NextRaceData;
import com.example.demo.f1.service.ErgastService;
import com.example.demo.prediction.port.NextRaceReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

@Component
public class NextRaceReaderAdapter implements NextRaceReader {

    private final ErgastService ergastService;

    public NextRaceReaderAdapter(ErgastService ergastService) {
        this.ergastService = ergastService;
    }

    @Override
    public NextRaceData findNextRace() throws JsonProcessingException {
        return ergastService.getNextRaceInfo();
    }
}
