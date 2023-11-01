package com.example.demo.repository;

import com.example.demo.model.fantasy.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictRepository extends JpaRepository<Prediction, Integer> {
}
