package com.example.demo.scoring.port;

import java.util.List;

public interface PredictionReader {
    List<PredictionData> findBySeasonAndRound(Integer season, Integer round);

    PredictionData findByUserIdAndRound(Integer userId, Integer round);
}
