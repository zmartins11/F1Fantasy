package com.example.cronJob;

import com.example.demo.model.RaceResultDto;
import com.example.demo.model.fantasy.Prediction;
import com.example.demo.model.fantasy.PredictionResult;
import com.example.demo.model.fantasy.RaceResult;
import com.example.demo.repository.PredictRepository;
import com.example.demo.repository.PredictionResultRepository;
import com.example.demo.repository.RaceResultRepository;
import com.example.demo.service.ErgastService;
import com.example.demo.service.PredictService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.*;

@Component
@Slf4j
public class ScheduleRaceResult {

    @Autowired
    private RaceResultRepository raceResultRepository;
    @Autowired
    private ErgastService ergastService;
    @Autowired
    private PredictRepository predictRepository;
    @Autowired
    private PredictService predictService;
    @Autowired
    private PredictionResultRepository predictionResultRepository;


    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void populateRaceResult() throws JsonProcessingException {
        try {
            RaceResultDto lastFinishedRaceResult = ergastService.getLastFinishedRaceResult();
            if (lastFinishedRaceResult == null) {
                log.debug("No finished race found");
                return;
            }

            Integer season = Integer.valueOf(lastFinishedRaceResult.getSeason());
            Integer round = lastFinishedRaceResult.getRound();
            RaceResult currentRace = raceResultRepository.findBySeasonAndRound(season, round);

            if (currentRace != null && currentRace.isPointsCalculated()) {
                log.debug("Skipping already processed race {}-{}", season, round);
                return;
            }

            if (currentRace == null) {
                updateRaceResult(lastFinishedRaceResult, season, round);
            }

            List<Prediction> predictions = predictRepository.findBySeasonAndRound(season, round);

            calculateAndSavePoints(currentRace, predictions);
        } catch (RestClientException exception) {
            log.warn("Could not retrieve the latest race result; it will be retried", exception);
        } catch (JsonProcessingException exception) {
            log.error("Could not parse the latest race result", exception);
        } catch (RuntimeException exception) {
            log.error("Unexpected error processing the latest race result", exception);
        }
    }

    private void updateRaceResult(RaceResultDto apiRaceResult, Integer season, Integer round) {
        RaceResult raceResultLastRace = new RaceResult();
        raceResultLastRace.setSeason(season);
        raceResultLastRace.setRound(round);
        raceResultLastRace.setRaceFinished(true);
        raceResultLastRace.setFirst(apiRaceResult.getFirst());
        raceResultLastRace.setSecond(apiRaceResult.getSecond());
        raceResultLastRace.setThird(apiRaceResult.getThird());
        raceResultLastRace.setFastestLap(apiRaceResult.getFastestLap());

        raceResultRepository.save(raceResultLastRace);
    }

    private void calculateAndSavePoints(
            RaceResult currentRace,
            List<Prediction> predictions) throws JsonProcessingException {
        for (Prediction prediction : predictions) {

            if (!predictionResultRepository.findByPredictionId(String.valueOf(prediction.getId())).isEmpty()) {
                continue;
            }

            int points = predictService.calculate(prediction, currentRace);

            savePredictionResult(prediction, currentRace, points);
        }

        currentRace.setPointsCalculated(true);
        raceResultRepository.save(currentRace);
    }

    private void savePredictionResult(
            Prediction prediction,
            RaceResult raceResult,
            int points) {

        PredictionResult result = new PredictionResult();
        result.setPredictionId(String.valueOf(prediction.getId()));
        result.setUserId(prediction.getUserId());
        result.setSeason(raceResult.getSeason());
        result.setRound(raceResult.getRound());
        result.setPoints(points);
        result.setShowPointsUser(Boolean.TRUE);

        predictionResultRepository.save(result);
    }
}
