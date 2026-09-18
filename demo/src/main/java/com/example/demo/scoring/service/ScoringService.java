package com.example.demo.scoring.service;

import com.example.demo.auth.model.User;
import com.example.demo.auth.repository.UserRepository;
import com.example.demo.scoring.dto.PointsInfoDto;
import com.example.demo.scoring.dto.TotalPointsDto;
import com.example.demo.f1.model.Driver;
import com.example.demo.f1.model.RaceResult;
import com.example.demo.f1.service.ErgastService;
import com.example.demo.f1.service.RaceResultService;
import com.example.demo.scoring.model.DriversPoints;
import com.example.demo.scoring.model.PredictionResult;
import com.example.demo.scoring.repository.DriversPointsRepository;
import com.example.demo.prediction.repository.PredictRepository;
import com.example.demo.scoring.repository.PredictionResultRepository;
import com.example.demo.scoring.port.PredictionData;
import com.example.demo.scoring.port.PredictionReader;
import com.example.demo.scoring.port.RaceResultData;
import com.example.demo.scoring.port.RaceResultReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ScoringService {

    private final PredictionResultRepository predictionResultRepository;
    private final DriversPointsRepository driversPointsRepository;
    private final PredictRepository predictRepository;
    private final RaceResultService raceResultService;

    private final ErgastService ergastService;
    private final UserRepository userRepository;
    private final PredictionReader predictionReader;
    private final RaceResultReader raceResultReader;

    public ScoringService(PredictionResultRepository predictionResultRepository,
                          DriversPointsRepository driversPointsRepository,
                          PredictRepository predictRepository,
                          RaceResultService raceResultService,
                          ErgastService ergastService,
                          UserRepository userRepository,
                          PredictionReader predictionReader,
                          RaceResultReader raceResultReader) {
        this.predictionResultRepository = predictionResultRepository;
        this.driversPointsRepository = driversPointsRepository;
        this.predictRepository = predictRepository;
        this.raceResultService = raceResultService;
        this.ergastService = ergastService;
        this.userRepository = userRepository;
        this.predictionReader = predictionReader;
        this.raceResultReader = raceResultReader;
    }

    public RaceResult getRacePassed() {
        return raceResultService.findTopByRaceFinishedTrueOrderByRoundDesc();
    }

    public Integer getAuthenticatedUserId(Authentication authentication) {
        return userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"))
                .getId();
    }

    public List<PointsInfoDto> getPointsInfo(Integer userId, String round) throws JsonProcessingException {

        RaceResult raceResult = raceResultService.findByRound(Integer.parseInt(round));

        if (raceResult == null) {
            return Collections.emptyList();
        }

        PredictionData prediction = predictionReader.findByUserIdAndRound(userId, Integer.parseInt(round));

        if (prediction == null) {
            return Collections.emptyList();
        }

        List<PredictionResult> predictionResults =
            predictionResultRepository.findByPredictionId(String.valueOf(prediction.id()));

        if (!predictionResults.isEmpty()) {
            PredictionResult predictionResult = predictionResults.get(0);
            predictionResult.setShowPointsUser(false);
            predictionResultRepository.save(predictionResult);
        }

        List<String> drivers = Stream.of(
                        prediction.first(),
                        prediction.second(),
                        prediction.third(),
                        prediction.fastestLap())
                .filter(Objects::nonNull)
                .toList();

        List<DriversPoints> driversPoints =
                driversPointsRepository.findByDriverInAndRaceId(
                        drivers,
                        String.valueOf(raceResult.getId()));

        Map<String, String> driverNames = ergastService.getDriversInSeason(String.valueOf(raceResult.getSeason()))
                .stream()
                .filter(driver -> driver.getPermanentNumber() != null)
                .collect(Collectors.toMap(
                        driver -> String.valueOf(driver.getPermanentNumber()),
                        Driver::getFamilyName,
                        (firstName, secondName) -> firstName));

        return driversPoints.stream()
                .map(driver -> {
                    PointsInfoDto dto = new PointsInfoDto();
                    dto.setDriver(driver.getDriver());
                    dto.setFamilyName(driverNames.get(driver.getDriver()));
                    dto.setPoints(driver.getPoints());
                    dto.setPosition(driver.getPosition());
                    return dto;
                })
                .toList();
    }


    public void calculateAndSavePoints(RaceResult currentRace) {
        RaceResultData raceResultData = raceResultReader.findBySeasonAndRound(
            currentRace.getSeason(), currentRace.getRound());

        if (raceResultData == null) {
            return;
        }

        List<PredictionData> predictions = predictionReader.findBySeasonAndRound(
            raceResultData.season(), raceResultData.round());

        for (PredictionData prediction : predictions) {

            if (!predictionResultRepository.findByPredictionId(String.valueOf(prediction.id())).isEmpty()) {
                continue;
            }

            int points = calculate(prediction, raceResultData);

            savePredictionResult(prediction, raceResultData, points);
        }

        currentRace.setPointsCalculated(true);
        raceResultService.save(currentRace);
    }

    private void savePredictionResult(
            PredictionData prediction,
            RaceResultData raceResult,
            int points) {

        PredictionResult result = new PredictionResult();
        result.setPredictionId(String.valueOf(prediction.id()));
        result.setUserId(prediction.userId());
        result.setSeason(raceResult.season());
        result.setRound(raceResult.round());
        result.setPoints(points);
        result.setShowPointsUser(Boolean.TRUE);

        predictionResultRepository.save(result);
    }

    private int calculate(PredictionData prediction, RaceResultData raceResult) {

        int points = 0;

        if (Boolean.TRUE.equals(prediction.predictedPodium())) {

            points += processDriver(
                    "1",
                    raceResult.id(),
                    prediction.first(),
                    raceResult.first());

            points += processDriver(
                    "2",
                    raceResult.id(),
                    prediction.second(),
                    raceResult.second());

            points += processDriver(
                    "3",
                    raceResult.id(),
                    prediction.third(),
                    raceResult.third());
        }

        if (Boolean.TRUE.equals(prediction.predictedFastestLap())) {

            int fastestLapPoints =
                    Objects.equals(prediction.fastestLap(), raceResult.fastestLap())
                            ? 5
                            : 0;

            createDriversPoints(
                    prediction.fastestLap(),
                    raceResult.id(),
                    fastestLapPoints,
                    "fastestLap");

            points += fastestLapPoints;
        }

        return points;
    }

    private int processDriver(String position, Integer raceId, String predictedDriver, String resultDriver) {

        int points = Objects.equals(predictedDriver, resultDriver) ? 5 : 0;

        createDriversPoints(predictedDriver, raceId, points, position);

        return points;
    }

    private void createDriversPoints(String driver, Integer raceResultId, int points, String position) {
        DriversPoints driversPoints = new DriversPoints();
        if (driversPointsRepository.findByRaceIdAndDriverAndPosition(String.valueOf(raceResultId), driver, position).isEmpty()) {
            driversPoints.setDriver(driver);
            driversPoints.setRaceId(raceResultId);
            driversPoints.setPoints(points);
            driversPoints.setPosition(position);
            driversPointsRepository.save(driversPoints);
        };
    }

    public List<TotalPointsDto> getTotalPoints() {
        List<TotalPointsDto> listUsers = new ArrayList<>();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<PredictionResult> predictsByUserTemp = predictionResultRepository.findByUserId(user.getId());
            if (!predictsByUserTemp.isEmpty()) {
                TotalPointsDto tmpP = new TotalPointsDto();
                tmpP.setUsername(user.getUserName());
                tmpP.setPoints(String.valueOf(predictionResultRepository.sumPointsByUserId(user.getId())));
                listUsers.add(tmpP);

            } else {
                TotalPointsDto tmp = new TotalPointsDto();
                tmp.setUsername(user.getUserName());
                tmp.setPoints("0");
                listUsers.add(tmp);
            }
        }
        //sorting the position
        listUsers.sort((o1, o2) -> Integer.compare(Integer.parseInt(o2.getPoints()), Integer.parseInt(o1.getPoints())));
        for (int i = 0; i < listUsers.size(); i++) {
            TotalPointsDto tmp = listUsers.get(i);
            tmp.setPosition(String.valueOf(i + 1));
        }
        return listUsers;
    }
}
