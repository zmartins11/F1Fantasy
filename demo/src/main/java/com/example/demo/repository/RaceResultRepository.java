package com.example.demo.repository;

import com.example.demo.model.fantasy.RaceResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RaceResultRepository extends JpaRepository<RaceResult, String> {

    public RaceResult findBySeasonAndRound(String season, String round);
}
