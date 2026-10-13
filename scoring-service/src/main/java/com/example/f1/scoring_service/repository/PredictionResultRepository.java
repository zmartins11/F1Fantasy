package com.example.f1.scoring_service.repository;

import com.example.f1.scoring_service.model.PredictionResult;
import com.example.f1.scoring_service.service.UserRankingProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PredictionResultRepository extends JpaRepository<PredictionResult, Integer> {

    List<PredictionResult> findByUserId(Integer userId);
    @Query("SELECT SUM(p.points) FROM PredictionResult p WHERE p.userId = :userId")
    Long sumPointsByUserId(Integer userId);

    List<PredictionResult> findByPredictionId(String id);

    boolean existsByPredictionId(String predictionId);

    @Query("""
    SELECT COALESCE(SUM(pr.points), 0)
    FROM PredictionResult pr
    WHERE pr.userId = :userId
      AND pr.season = :season""")
    Long getTotalPointsByUserAndSeason(@Param("userId") Integer userId, @Param("season") Integer season);

    @Query("""
    SELECT
        pr.userId AS userId,
        SUM(pr.points) AS totalPoints
    FROM PredictionResult pr
    WHERE pr.season = :season
    GROUP BY pr.userId
    ORDER BY SUM(pr.points) DESC, pr.userId ASC
""")
    List<UserRankingProjection> getSeasonRanking(
            @Param("season") Integer season
    );

}
