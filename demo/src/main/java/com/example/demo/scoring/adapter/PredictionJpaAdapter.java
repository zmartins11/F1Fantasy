package com.example.demo.scoring.adapter;

import com.example.demo.prediction.model.Prediction;
import com.example.demo.prediction.repository.PredictRepository;
import com.example.demo.scoring.port.PredictionData;
import com.example.demo.scoring.port.PredictionReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PredictionJpaAdapter implements PredictionReader {

    private final PredictRepository predictRepository;

    public PredictionJpaAdapter(PredictRepository predictRepository) {
        this.predictRepository = predictRepository;
    }

    @Override
    public List<PredictionData> findBySeasonAndRound(Integer season, Integer round) {
        return predictRepository.findBySeasonAndRound(season, round)
                .stream()
                .map(this::toData)
                .toList();
    }

    @Override
        public PredictionData findByUserIdAndRound(Integer userId, Integer round) {
                return predictRepository.findByUserIdAndRound(userId, round)
                                .map(this::toData)
                                .orElse(null);
    }

    private PredictionData toData(Prediction prediction) {
        return new PredictionData(
                prediction.getId(),
                prediction.getUserId(),
                prediction.getSeason(),
                prediction.getRound(),
                prediction.getFirst(),
                prediction.getSecond(),
                prediction.getThird(),
                prediction.getFastestLap(),
                prediction.getPredictedPodium(),
                prediction.getPredictedFastestLap());
    }
}
