package com.example.f1.f1_service.service;

import com.example.f1.f1_service.contracts.race.RaceResultData;
import com.example.f1.f1_service.model.entity.RaceResult;
import com.example.f1.f1_service.repository.RaceResultRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public RaceResultData findTopByRaceFinishedTrueOrderByRoundDesc() {
        return toRaceResultData(repository.findTopByRaceFinishedTrueOrderByRoundDesc());
    }

    private RaceResultData toRaceResultData(RaceResult raceResult) {
        return new RaceResultData(raceResult.getId(),
                raceResult.getSeason(),
                raceResult.getRound(),
                raceResult.getFirst(),
                raceResult.getSecond(),
                raceResult.getThird(),
                raceResult.getFastestLap());
    }

    public RaceResult findByRound(int round) {
        return repository.findByRound(round);
    }

    public RaceResult save(RaceResult raceResult) {
        return repository.save(raceResult);
    }

    @Transactional
    public void markPointsCalculated(Integer raceResultId) {
        RaceResult raceResult = repository.findById(raceResultId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Race result not found: " + raceResultId));

        raceResult.setPointsCalculated(true);
    }
}
