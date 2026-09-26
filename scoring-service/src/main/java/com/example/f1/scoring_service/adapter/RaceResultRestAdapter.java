package com.example.f1.scoring_service.adapter;

import com.example.f1.scoring_service.contracts.prediction.PredictionData;
import com.example.f1.scoring_service.contracts.race.RaceResultData;
import com.example.f1.scoring_service.contracts.race.RaceResultReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RaceResultRestAdapter implements RaceResultReader {

    private final RestClient restClient;

    public RaceResultRestAdapter(
            @Value("${services.f1.url}") String predictionServiceUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(predictionServiceUrl)
                .build();
    }

    @Override
    public RaceResultData latestRaceFinished() {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/internal/f1/latestRaceFinished")
                            .build())
                    .retrieve()
                    .body(RaceResultData.class);
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public RaceResultData findByRound(int round) {
        return null;
    }
}
