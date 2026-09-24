package com.example.demo.prediction.service;

import com.example.demo.f1.dto.NextRaceData;
import com.example.demo.auth.port.UserReader;
import com.example.demo.prediction.dto.PredictionDto;
import com.example.demo.prediction.dto.RaceScheduleResponse;
import com.example.demo.prediction.dto.UserPredictionData;

import com.example.demo.prediction.model.Prediction;
import com.example.demo.prediction.repository.PredictRepository;
import com.example.demo.prediction.port.NextRaceReader;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class PredictService {

    private final PredictRepository predictRepository;

    private final UserReader userReader;
    private final NextRaceReader nextRaceReader;



    public PredictService(PredictRepository predictRepository,
                          UserReader userReader, NextRaceReader nextRaceReader) {
        this.predictRepository = predictRepository;
        this.userReader = userReader;
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

    public RaceScheduleResponse getRaceSchedule(Integer userId) throws JsonProcessingException {
        NextRaceData nextRace = nextRaceReader.findNextRace();
        UserPredictionData prediction = getUserPrediction(nextRace.round(), userId);
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


    public Integer getAuthenticatedUserId(Authentication authentication) {
        return userReader.findByUserName(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"))
                .id();
    }
}
