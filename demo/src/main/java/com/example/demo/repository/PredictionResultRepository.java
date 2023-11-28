package com.example.demo.repository;

import com.example.demo.model.fantasy.PredictionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PredictionResultRepository extends JpaRepository<PredictionResult, Integer> {

    public List<PredictionResult> findByUserId(String userId);
    @Query("SELECT SUM(p.points) FROM PredictionResult p WHERE p.userId = :userId")
    Long sumPointsByUserId(String userId);
    @Query("SELECT p.userId, SUM(p.points) FROM PredictionResult p GROUP BY p.userId ORDER BY SUM(p.points) DESC")
    List<Object[]> findTotalPointsByUser();

}
