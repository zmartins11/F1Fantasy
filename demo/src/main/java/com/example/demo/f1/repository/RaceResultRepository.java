package com.example.demo.f1.repository;

import com.example.demo.f1.model.RaceResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaceResultRepository extends JpaRepository<RaceResult, Integer> {

    RaceResult findBySeasonAndRound(Integer season, Integer round);
    List<RaceResult> findBySeason(Integer season);
    RaceResult findTopByRaceFinishedFalseOrderByRoundAsc();
    RaceResult findByRound(Integer round);
    RaceResult findTopByRaceFinishedTrueOrderByRoundDesc();
}
