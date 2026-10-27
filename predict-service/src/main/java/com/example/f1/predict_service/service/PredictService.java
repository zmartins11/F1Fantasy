package com.example.f1.predict_service.service;

import com.example.f1.predict_service.contracts.prediction.PredictionData;
import com.example.f1.predict_service.contracts.race.NextRaceData;
import com.example.f1.predict_service.dto.PredictionDto;
import com.example.f1.predict_service.dto.PredictionStatsDto;
import com.example.f1.predict_service.dto.RaceScheduleResponse;
import com.example.f1.predict_service.dto.UserPredictionData;
import com.example.f1.predict_service.model.Prediction;
import com.example.f1.predict_service.contracts.race.NextRaceReader;
import com.example.f1.predict_service.repository.PredictRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PredictService {

    private final PredictRepository predictRepository;

    private final NextRaceReader nextRaceReader;


    public PredictService(PredictRepository predictRepository,
                          NextRaceReader nextRaceReader) {
        this.predictRepository = predictRepository;
        this.nextRaceReader = nextRaceReader;
    }

    public Prediction savePrediction(PredictionDto dto) {

        Prediction prediction = predictRepository
                .findByUserIdAndRound(dto.getUserId(), dto.getRound())
                .orElseGet(() -> createNewPrediction(dto));

        if (dto.getFirst() != null) {
            prediction.setFirst(dto.getFirst());
            prediction.setSecond(dto.getSecond());
            prediction.setThird(dto.getThird());
            prediction.setPredictedPodium(true);
        }

        if (dto.getFastestLap() != null) {
            prediction.setFastestLap(dto.getFastestLap());
            prediction.setPredictedFastestLap(true);
        }

        return predictRepository.save(prediction);
    }


    private Prediction createNewPrediction(PredictionDto dto) {

        Prediction prediction = new Prediction();

        prediction.setUserId(dto.getUserId());
        prediction.setRound(dto.getRound());

        prediction.setPredictedPodium(false);
        prediction.setPredictedFastestLap(false);
        prediction.setSeason(dto.getSeason());

        return prediction;
    }

    public RaceScheduleResponse getRaceSchedule(Long userId) throws JsonProcessingException {
        NextRaceData nextRace = nextRaceReader.findNextRace();
        UserPredictionData prediction = getUserPrediction(nextRace.round(), userId.intValue());
        return new RaceScheduleResponse(nextRace, prediction);
    }

    private UserPredictionData getUserPrediction(String round, Integer userId) {
        return predictRepository.findByUserIdAndRound(userId, Integer.parseInt(round))
                .map(prediction -> new UserPredictionData(
                        true,
                        prediction.getFirst(),
                        prediction.getSecond(),
                        prediction.getThird(),
                        prediction.getFastestLap(),
                        prediction.getPredictedPodium(),
                        prediction.getPredictedFastestLap()))
                .orElseGet(() -> new UserPredictionData(false, null, null, null, null, null, null));
    }

    public List<PredictionData> findBySeasonAndRound(Integer season, Integer round) {
        return toPredictionDataList(predictRepository.findBySeasonAndRound(season, round));
    }


    public Optional<PredictionData> findByUserIdAndSeasonAndRound(Integer userId, Integer season, Integer round) {
        return predictRepository.findByUserIdAndSeasonAndRound(userId,season, round)
                .map(this::toPredictionData);
    }

    private List<PredictionData> toPredictionDataList(
            List<Prediction> predictions) {

        return predictions.stream()
                .map(this::toPredictionData)
                .toList();
    }

    private PredictionData toPredictionData(Prediction prediction) {
        return new PredictionData(
                prediction.getId(),
                prediction.getUserId(),
                prediction.getSeason(),
                prediction.getRound(),
                prediction.getFirst(),
                prediction.getSecond(),
                prediction.getThird(),
                prediction.getFastestLap(),
                prediction.getPredictedPodium(),
                prediction.getPredictedFastestLap()
        );
    }

    @Transactional(readOnly = true)
    public PredictionStatsDto getPredictionStatsBySeason(Long userId, Integer season) {
        return new PredictionStatsDto(predictRepository.countByUserIdAndSeason(userId.intValue(), season));
    }
}
