package com.example.demo.repository;

import com.example.demo.model.fantasy.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PredictRepository extends JpaRepository<Prediction, Integer> {
    public Prediction findByUserIdAndRaceId(String userId, String raceId);
    public List<Prediction> findByRaceId(String raceId);

    public Prediction findByUserIdAndRound(String userId, String round);
}
