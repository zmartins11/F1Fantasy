package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.exception.SavePredictionException;
import com.example.demo.model.fantasy.Prediction;
import com.example.demo.model.fantasy.RaceResult;
import com.example.demo.service.ErgastService;
import com.example.demo.service.PredictService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
public class PredictController {

    @Autowired
    PredictService predictService;
    @Autowired
    private ErgastService ergastService;

    @PostMapping("/predict")
    private PredictionDto savePrediction(@RequestBody PredictionDto prediction) throws Exception {
        TimeUnit.SECONDS.sleep(2);
        //season static value
        Year currentYear = Year.now();
        String season = String.valueOf(currentYear.getValue());
        //check if raceFinished
        //TODO: GET THE ROUND STRING FOR PREDICTION OBJECT
//        boolean raceFinished = predictService.checkRaceFinished(season, prediction.getRound());
//        if (raceFinished) {
//            throw new SavePredictionException("race already finish");
//        }
        Prediction savedPrediction = predictService.savePrediction(prediction);
        prediction.setPredictedPodium(savedPrediction.getPredictedPodium());
        prediction.setPredictedFastestLap(savedPrediction.getPredictedFastestLap());
        prediction.setFirst(savedPrediction.getFirst());
        prediction.setSecond(savedPrediction.getSecond());
        prediction.setThird(savedPrediction.getThird());
        prediction.setFastestLap(savedPrediction.getFastestLap());
        return prediction;
    }


    @GetMapping("/raceSchedule")
    private NextRaceInfoDto getRaceInfo(@RequestParam String username) throws JsonProcessingException, InterruptedException {
        RaceResult nextRaceInfo = predictService.getNextRaceInfo();
        NextRaceInfoDto nextRaceInfoDto = ergastService.getScheduleRace(nextRaceInfo);

        //houve atualizacao do predictionLocked
//        if (!nextRaceInfoDto.getPredictionLocked().equals(nextRaceInfo.get().isPredictionLocked())) {
//            nextRaceInfo.get().setPredictionLocked(nextRaceInfoDto.getPredictionLocked());
//            predictService.updatePredictionLocked(nextRaceInfoDto);
//        }
        //checkUserPredictions
        nextRaceInfoDto = predictService.getUserPrediction(nextRaceInfoDto, username);

        return nextRaceInfoDto;
    }

    @GetMapping("/pointsInfo")
    private List<PointsInfoDto> getPointsInfo(@RequestParam String username) {
        RaceResult racedPassed = predictService.getRacePassed();
        if (racedPassed != null) {
            return predictService.getPointsInfo(username, String.valueOf(racedPassed.getRound()));
        } else {
            return null;
        }
    }

    //check if there are a raceCompleted before the currentOne
    //check if the user has a prediction for that race
    //on predictionResult set column boolean : showPointsUser : false

    @GetMapping("/totalPoints")
    private List<TotalPointsDto> getTotalPoints(@RequestParam String username) {
        return predictService.getTotalPoints();
    }

    @GetMapping("/totalPointsByUser")
    private TotalPointsDto getTotalPointsByUser(@RequestParam String username) {
        return predictService.getTotalPointsByUser(username);
    }

    @GetMapping("/standings")
    private StandingsDto standingsSeason() throws JsonProcessingException {
        return ergastService.getStandings();
    }

}
