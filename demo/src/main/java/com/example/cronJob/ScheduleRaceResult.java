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

import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        Set<String> checkedRaces = new HashSet<>();

        for (Prediction prediction : predictRepository.findAll()) {
            if (prediction.getSeason() == null || prediction.getRound() == null) {
                continue;
            }

            Integer season = prediction.getSeason();
            Integer round = prediction.getRound();
            String raceKey = season + "-" + round;

            if (!checkedRaces.add(raceKey)) {
                continue;
            }

            RaceResult currentRace = raceResultRepository.findBySeasonAndRound(season, round);
            if (currentRace != null && currentRace.isPointsCalculated()) {
                log.debug("Skipping already processed race {}-{}", season, round);
                continue;
            }

            try {
                RaceResultDto apiRaceResult = ergastService.getRaceResult(
                    String.valueOf(season),
                    String.valueOf(round));

                if (apiRaceResult == null || !apiRaceResult.isRaceFinished()) {
                    log.debug("Race {}-{} has no final result yet", season, round);
                    continue;
                }

                if (currentRace == null) {
                    currentRace = new RaceResult();
                    currentRace.setSeason(season);
                    currentRace.setRound(round);
                }

                updateRaceResult(currentRace, apiRaceResult);
                calculatePoints(currentRace);
            } catch (RestClientException exception) {
                log.warn("Could not retrieve result for race {}-{}; it will be retried", season, round, exception);
            } catch (JsonProcessingException exception) {
                log.error("Could not parse result for race {}-{}", season, round, exception);
            } catch (RuntimeException exception) {
                log.error("Unexpected error processing race {}-{}", season, round, exception);
            }
        }
    }

    private void updateRaceResult(RaceResult currentRace, RaceResultDto apiRaceResult) {
        currentRace.setRaceFinished(true);
        currentRace.setFirst(apiRaceResult.getFirst());
        currentRace.setSecond(apiRaceResult.getSecond());
        currentRace.setThird(apiRaceResult.getThird());
        currentRace.setFastestLap(apiRaceResult.getFastestLap());

        raceResultRepository.save(currentRace);
    }

    private void calculatePoints(RaceResult currentRace) throws JsonProcessingException {

        List<Prediction> predictions =
                predictRepository.findBySeasonAndRound(
                        currentRace.getSeason(),
                        currentRace.getRound());

        Map<String, Integer> driverPoints = predictService.buildDriverPoints(currentRace);

        for (Prediction prediction : predictions) {

            int points = predictService.calculate(prediction, currentRace, driverPoints);

            PredictionResult result = new PredictionResult();
            result.setPredictionId(String.valueOf(prediction.getId()));
            result.setUserId(prediction.getUserId());
            result.setSeason(currentRace.getSeason());
            result.setRound(currentRace.getRound());
            result.setPoints(points);
            result.setShowPointsUser(Boolean.TRUE);

            predictionResultRepository.save(result);
        }

        currentRace.setPointsCalculated(true);
        raceResultRepository.save(currentRace);
    }
}
