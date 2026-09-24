package com.example.f1.scoring_service.service;


import com.example.f1.scoring_service.contracts.driver.DriverData;
import com.example.f1.scoring_service.contracts.driver.DriverReader;
import com.example.f1.scoring_service.contracts.race.RaceFinishedEvent;
import com.example.f1.scoring_service.contracts.race.RaceResultData;
import com.example.f1.scoring_service.contracts.user.UserData;
import com.example.f1.scoring_service.contracts.user.UserReader;
import com.example.f1.scoring_service.dto.PointsInfoDto;
import com.example.f1.scoring_service.dto.TotalPointsDto;
import com.example.f1.scoring_service.model.DriversPoints;
import com.example.f1.scoring_service.model.PredictionResult;
import com.example.f1.scoring_service.contracts.prediction.PredictionData;
import com.example.f1.scoring_service.contracts.prediction.PredictionReader;
import com.example.f1.scoring_service.contracts.race.RaceResultReader;
import com.example.f1.scoring_service.contracts.race.RaceResultWriter;
import com.example.f1.scoring_service.repository.DriversPointsRepository;
import com.example.f1.scoring_service.repository.PredictionResultRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ScoringService {

    private final PredictionResultRepository predictionResultRepository;
    private final DriversPointsRepository driversPointsRepository;

    private final DriverReader driverReader;
    private final PredictionReader predictionReader;
    private final RaceResultReader raceResultReader;
    private final RaceResultWriter raceResultWriter;

    private final UserReader userReader;

    public ScoringService(PredictionResultRepository predictionResultRepository,
                          DriversPointsRepository driversPointsRepository,
                          PredictionReader predictionReader,
                          RaceResultReader raceResultReader,
                          RaceResultWriter raceResultWriter,
                          UserReader userReader,
                          DriverReader driverReader) {
        this.predictionResultRepository = predictionResultRepository;
        this.driversPointsRepository = driversPointsRepository;
        this.predictionReader = predictionReader;
        this.raceResultReader = raceResultReader;
        this.raceResultWriter = raceResultWriter;
        this.userReader = userReader;
        this.driverReader = driverReader;
    }

    public RaceResultData getRacePassed() {
        return raceResultReader.findTopByRaceFinishedTrueOrderByRoundDesc();
    }

    public Integer getAuthenticatedUserId(Authentication authentication) {
        return userReader.findByUserName(authentication.name())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"))
                .id();
    }

    public List<PointsInfoDto> getPointsInfo(Integer userId, String round) throws JsonProcessingException {

        RaceResultData raceResult = raceResultReader.findByRound(Integer.parseInt(round));

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
                        String.valueOf(raceResult.id()));

        Map<String, String> driverNames = driverReader.findBySeason(String.valueOf(raceResult.season()))
                .stream()
            .filter(driver -> driver.permanentNumber() != null)
                .collect(Collectors.toMap(
                driver -> String.valueOf(driver.permanentNumber()),
                DriverData::familyName,
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


    public void calculateAndSavePoints(RaceFinishedEvent event) {

        List<PredictionData> predictions = predictionReader.findBySeasonAndRound(
            event.season(), event.round());

        for (PredictionData prediction : predictions) {

            if (!predictionResultRepository.findByPredictionId(String.valueOf(prediction.id())).isEmpty()) {
                continue;
            }

            int points = calculate(prediction, event);

            savePredictionResult(prediction, event, points);
        }

        raceResultWriter.markPointsCalculated(event.raceResultId());
    }

    private void savePredictionResult(
            PredictionData prediction,
            RaceFinishedEvent raceResultEvent,
            int points) {

        PredictionResult result = new PredictionResult();
        result.setPredictionId(String.valueOf(prediction.id()));
        result.setUserId(prediction.userId());
        result.setSeason(raceResultEvent.season());
        result.setRound(raceResultEvent.round());
        result.setPoints(points);
        result.setShowPointsUser(Boolean.TRUE);

        predictionResultRepository.save(result);
    }

    private int calculate(PredictionData prediction, RaceFinishedEvent event) {

        int points = 0;

        if (Boolean.TRUE.equals(prediction.predictedPodium())) {

            points += processDriver(
                    "1",
                    event.raceResultId(),
                    prediction.first(),
                    event.first());

            points += processDriver(
                    "2",
                    event.raceResultId(),
                    prediction.second(),
                    event.second());

            points += processDriver(
                    "3",
                    event.raceResultId(),
                    prediction.third(),
                    event.third());
        }

        if (Boolean.TRUE.equals(prediction.predictedFastestLap())) {

            int fastestLapPoints =
                    Objects.equals(prediction.fastestLap(), event.fastestLap())
                            ? 5
                            : 0;

            createDriversPoints(
                    prediction.fastestLap(),
                    event.raceResultId(),
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
        List<UserData> users = userReader.findAll();

        for (UserData user : users) {
            List<PredictionResult> predictsByUserTemp = predictionResultRepository.findByUserId(user.id());
            if (!predictsByUserTemp.isEmpty()) {
                TotalPointsDto tmpP = new TotalPointsDto();
                tmpP.setUsername(user.userName());
                tmpP.setPoints(String.valueOf(predictionResultRepository.sumPointsByUserId(user.id())));
                listUsers.add(tmpP);

            } else {
                TotalPointsDto tmp = new TotalPointsDto();
                tmp.setUsername(user.userName());
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
