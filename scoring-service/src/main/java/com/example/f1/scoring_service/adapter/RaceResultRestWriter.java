package com.example.f1.scoring_service.adapter;

import com.example.f1.scoring_service.contracts.driver.DriverData;
import com.example.f1.scoring_service.contracts.race.RaceResultWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RaceResultRestWriter implements RaceResultWriter {

    private final RestClient restClient;

    public RaceResultRestWriter(
            @Value("${services.f1.url}") String predictionServiceUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(predictionServiceUrl)
                .build();
    }

    @Override
    public void markPointsCalculated(Integer raceResultId) {
        restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/internal/f1/{raceResultId}/pointsCalculated")
                        .build(raceResultId))
                .retrieve()
                .toBodilessEntity();
    }
}
