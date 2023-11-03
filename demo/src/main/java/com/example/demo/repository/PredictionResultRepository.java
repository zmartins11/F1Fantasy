package com.example.demo.repository;

import com.example.demo.model.fantasy.PredictionResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionResultRepository extends JpaRepository<PredictionResult, Integer> {
}
