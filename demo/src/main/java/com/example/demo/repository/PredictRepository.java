package com.example.demo.repository;

import com.example.demo.model.fantasy.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PredictRepository extends JpaRepository<Prediction, Integer> {
    Optional<Prediction> findByUserIdAndRound(String userId, String round);
}
