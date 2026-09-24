package com.example.f1.predict_service.adapter;

import com.example.f1.predict_service.contracts.race.NextRaceData;
import com.example.f1.predict_service.contracts.race.NextRaceReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

@Component
public class NextRaceRestAdapter implements NextRaceReader {


    public NextRaceRestAdapter() {}

    @Override
    public NextRaceData findNextRace() {
        //return ergastService.getNextRaceInfo();
        return null; //TODO : chamda rest ao service da f1
    }
}
