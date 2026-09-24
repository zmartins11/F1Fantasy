package com.example.f1.f1_service.service;

import com.example.f1.f1_service.model.entity.RaceResult;
import com.example.f1.f1_service.repository.RaceResultRepository;
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

    public RaceResult findById(Integer id) {
        return repository.findById(id).orElse(null);
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
