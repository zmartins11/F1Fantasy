package com.example.f1.scoring_service.adapter;

import com.example.f1.scoring_service.contracts.prediction.PredictionData;
import com.example.f1.scoring_service.contracts.prediction.PredictionReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@Slf4j
public class PredictionRestAdapter implements PredictionReader {

    private final RestClient restClient;

    public PredictionRestAdapter(
            @Value("${services.prediction.url}") String predictionServiceUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(predictionServiceUrl)
                .build();
    }
    @Override
    public List<PredictionData> findBySeasonAndRound(Integer season, Integer round) {

        log.info(
                "Calling prediction-service for season={}, round={}",
                season,
                round
        );

        PredictionData[] predictions = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/internal/predictions")
                        .queryParam("season", season)
                        .queryParam("round", round)
                        .build())
                .retrieve()
                .body(PredictionData[].class);

        log.info(
                "Prediction-service returned {} predictions",
                predictions == null ? 0 : predictions.length
        );

        return predictions == null ? List.of() : List.of(predictions);
    }

    @Override
    public PredictionData findByUserIdAndRound(
            Integer userId,
            Integer round) {

        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/internal/predictions/user")
                            .queryParam("userId", userId)
                            .queryParam("round", round)
                            .build())
                    .retrieve()
                    .body(PredictionData.class);
        } catch (Exception exception) {
            return null;
        }
    }
}
