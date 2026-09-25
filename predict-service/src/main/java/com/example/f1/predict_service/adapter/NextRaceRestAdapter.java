package com.example.f1.predict_service.adapter;

import com.example.f1.predict_service.contracts.race.NextRaceData;
import com.example.f1.predict_service.contracts.race.NextRaceReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NextRaceRestAdapter implements NextRaceReader {

    private final RestClient restClient;


    public NextRaceRestAdapter(
            @Value("${services.f1.url}") String authServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(authServiceUrl)
                .build();
    }

    @Override
    public NextRaceData findNextRace() {
        NextRaceData nextRaceData = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/internal/f1")
                        .build())
                .retrieve()
                .body(NextRaceData.class);

        if (nextRaceData == null) {
            throw new IllegalStateException(
                    "F1 service returned an empty next-race response");
        }
        return nextRaceData;
    }
}
