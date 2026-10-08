package com.example.f1.f1_service.repository;


import com.example.f1.f1_service.model.entity.RaceResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaceResultRepository extends JpaRepository<RaceResult, Integer> {

    RaceResult findBySeasonAndRound(Integer season, Integer round);
    List<RaceResult> findBySeason(Integer season);
    RaceResult findTopByRaceFinishedFalseOrderByRoundAsc();
    RaceResult findByRound(Integer round);
    RaceResult findTopByRaceFinishedTrueOrderByRoundDesc();
}
