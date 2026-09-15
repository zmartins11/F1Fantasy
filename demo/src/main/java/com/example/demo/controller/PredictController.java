package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.Race;
import com.example.demo.model.fantasy.Prediction;
import com.example.demo.model.fantasy.RaceResult;
import com.example.demo.service.ErgastService;
import com.example.demo.service.PredictService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
public class PredictController {

    private final PredictService predictService;
    private final ErgastService ergastService;

    public PredictController(PredictService predictService, ErgastService ergastService) {
        this.predictService = predictService;
        this.ergastService = ergastService;
    }

    @PostMapping("/predict")
    public ResponseEntity<PredictionDto> savePrediction(@RequestBody PredictionDto prediction) {
        //TimeUnit.SECONDS.sleep(1);
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
        prediction.setUserId(savedPrediction.getUserId());
        return ResponseEntity.ok(prediction);
    }


    @GetMapping("/raceSchedule")
    public ResponseEntity<NextRaceInfoDto> getNextRaceInfo(@RequestParam Integer userId) throws JsonProcessingException {
        //TimeUnit.SECONDS.sleep(3);
        //RaceResult nextRaceInfo = predictService.getNextRaceInfo();
        NextRaceInfoDto nextRaceInfoDto = ergastService.getNextRaceInfo();


        //checkUserPredictions
        nextRaceInfoDto = predictService.getUserPrediction(nextRaceInfoDto, userId);

        return ResponseEntity.ok(nextRaceInfoDto);
    }

    @GetMapping("/nextRaceDetails")
    public ResponseEntity<Race> getNextRaceDetails() throws JsonProcessingException {
        return ResponseEntity.ok(ergastService.getNextRace());
    }

    @GetMapping("/pointsInfo")
    public ResponseEntity<List<PointsInfoDto>> getPointsInfo(@RequestParam Integer userId) throws JsonProcessingException {
        RaceResult racedPassed = predictService.getRacePassed();
        if (racedPassed != null) {
            return ResponseEntity.ok(predictService.getPointsInfo(userId, String.valueOf(racedPassed.getRound())));
        } else {
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/totalPoints")
    public ResponseEntity<List<TotalPointsDto>> getTotalPoints(@RequestParam String username) {
        return ResponseEntity.ok(predictService.getTotalPoints());
    }

    @GetMapping("/standings")
    public ResponseEntity<StandingsDto> standingsSeason() throws JsonProcessingException {
        return ResponseEntity.ok(ergastService.getStandings());
    }


    @GetMapping("/allRaces")
    public ResponseEntity<List<RaceInfo>> getAllRaces() throws JsonProcessingException {
        return ResponseEntity.ok(ergastService.getAllRaces(Year.now().toString()));
    }

}
