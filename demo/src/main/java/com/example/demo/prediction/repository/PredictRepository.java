package com.example.demo.prediction.repository;

import com.example.demo.prediction.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PredictRepository extends JpaRepository<Prediction, Integer> {
    Optional<Prediction> findByUserIdAndRound(Integer userId, Integer round);

    List<Prediction> findBySeasonAndRound(Integer season, Integer round);
}
