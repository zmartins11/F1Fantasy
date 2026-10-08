package com.example.f1.scoring_service.adapter;

import com.example.f1.scoring_service.contracts.driver.DriverData;
import com.example.f1.scoring_service.contracts.driver.DriverReader;
import com.example.f1.scoring_service.contracts.prediction.PredictionData;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class F1DriverRestReader implements DriverReader {

    private final RestClient restClient;

    public F1DriverRestReader(
            @Value("${services.f1.url}") String predictionServiceUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(predictionServiceUrl)
                .build();
    }


    @Override
    public List<DriverData> findBySeason(String season) throws JsonProcessingException {
        DriverData[] drivers = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/internal/f1/drivers/{season}")
                        .build(season))
                .retrieve()
                .body(DriverData[].class);

        return drivers == null ? List.of() : List.of(drivers);
    }
}
