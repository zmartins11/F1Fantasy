package com.example.f1.predict_service.repository;


import com.example.f1.predict_service.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PredictRepository extends JpaRepository<Prediction, Integer> {
    Optional<Prediction> findByUserIdAndRound(Integer userId, Integer round);

    List<Prediction> findBySeasonAndRound(Integer season, Integer round);
}
