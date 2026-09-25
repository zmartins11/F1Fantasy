package com.example.demo.scoring.adapter;

import com.example.demo.scoring.port.PredictionData;
import com.example.demo.scoring.port.PredictionReader;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Primary
public class PredictionRestAdapter implements PredictionReader {

    @Override
    public List<PredictionData> findBySeasonAndRound(Integer season, Integer round) {
        return null;
    }

    @Override
    public PredictionData findByUserIdAndRound(Integer userId, Integer round) {
        return null;
    }
}
