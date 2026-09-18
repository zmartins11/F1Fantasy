package com.example.demo.scoring.repository;

import com.example.demo.scoring.model.PredictionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PredictionResultRepository extends JpaRepository<PredictionResult, Integer> {

    List<PredictionResult> findByUserId(Integer userId);
    @Query("SELECT SUM(p.points) FROM PredictionResult p WHERE p.userId = :userId")
    Long sumPointsByUserId(Integer userId);

    List<PredictionResult> findByPredictionId(String id);

}
