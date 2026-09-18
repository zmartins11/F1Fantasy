package com.example.demo.f1.service;

import com.example.demo.f1.model.RaceResult;
import com.example.demo.f1.repository.RaceResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceResultService {

    private final RaceResultRepository repository;

    public RaceResultService(RaceResultRepository repository) {
        this.repository = repository;
    }

    public RaceResult findBySeasonAndRound(Integer season, Integer round) {
        return repository.findBySeasonAndRound(season, round);
    }

    public RaceResult findLastFinishedRace() {
        return repository.findTopByRaceFinishedTrueOrderByRoundDesc();
    }

    public List<RaceResult> findBySeason(Integer season) {
        return repository.findBySeason(season);

    }

    public RaceResult findTopByRaceFinishedTrueOrderByRoundDesc() {
        return repository.findTopByRaceFinishedTrueOrderByRoundDesc();
    }

    public RaceResult findByRound(int round) {
        return repository.findByRound(round);
    }

    public RaceResult save(RaceResult raceResult) {
        return repository.save(raceResult);
    }
}
