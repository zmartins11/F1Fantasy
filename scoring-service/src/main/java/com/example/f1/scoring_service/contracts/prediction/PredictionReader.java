package com.example.f1.scoring_service.contracts.prediction;

import com.example.f1.scoring_service.contracts.prediction.PredictionData;

import java.util.List;

public interface PredictionReader {
    List<PredictionData> findBySeasonAndRound(Integer season, Integer round);

    PredictionData findByUserIdAndRound(Integer userId, Integer round);
}
