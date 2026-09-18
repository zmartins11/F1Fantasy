package com.example.demo.prediction.service;

import com.example.demo.auth.repository.UserRepository;
import com.example.demo.prediction.dto.NextRaceInfoDto;
import com.example.demo.prediction.dto.PredictionDto;

import com.example.demo.prediction.model.Prediction;
import com.example.demo.prediction.repository.PredictRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class PredictService {

    private final PredictRepository predictRepository;
    private final UserRepository userRepository;

    public PredictService (PredictRepository predictRepository,UserRepository userRepository ) {
        this.predictRepository = predictRepository;
        this.userRepository = userRepository;
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

    public NextRaceInfoDto getUserPrediction(NextRaceInfoDto nextRaceInfoDto, Integer userId) {
        predictRepository.findByUserIdAndRound(userId, Integer.parseInt(nextRaceInfoDto.getRound()))
                .ifPresentOrElse(userPrediction -> {

                    nextRaceInfoDto.setUserHavePrediction(true);
                    nextRaceInfoDto.setFirst(userPrediction.getFirst());
                    nextRaceInfoDto.setSecond(userPrediction.getSecond());
                    nextRaceInfoDto.setThird(userPrediction.getThird());

                    if (Boolean.TRUE.equals(userPrediction.getPredictedFastestLap())) {
                        nextRaceInfoDto.setFastestLap(userPrediction.getFastestLap());
                    }
                    nextRaceInfoDto.setPredictedPodium(userPrediction.getPredictedPodium());
                    nextRaceInfoDto.setPredictedFastestLap(userPrediction.getPredictedFastestLap());

                }, () -> nextRaceInfoDto.setUserHavePrediction(false));
        return nextRaceInfoDto;
    }


    public Integer getAuthenticatedUserId(Authentication authentication) {
        return userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"))
                .getId();
    }
}
