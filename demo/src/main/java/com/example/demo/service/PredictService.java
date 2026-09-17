package com.example.demo.service;

import com.example.demo.dto.NextRaceInfoDto;
import com.example.demo.dto.PointsInfoDto;
import com.example.demo.dto.PredictionDto;
import com.example.demo.dto.TotalPointsDto;
import com.example.demo.model.Driver;
import com.example.demo.model.fantasy.*;
import com.example.demo.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PredictService {

    @Autowired
    private RaceResultRepository raceResultRepository;
    @Autowired
    private PredictRepository predictRepository;
    @Autowired
    private PredictionResultRepository predictionResultRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DriversPointsRepository driversPointsRepository;
    @Autowired
    private ErgastService ergastService;

    public RaceResult findBySeasonAndRound(String season, String round) {
        return raceResultRepository.findBySeasonAndRound(
                Integer.parseInt(season),
                Integer.parseInt(round));
    }


    public RaceResult getRace(String season, String round) {
        return findBySeasonAndRound(season, round);
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


    // método chamado quando o RaceResult com o race_id da prediction é preenchido
    public int calculate(Prediction prediction, RaceResult raceResult) {

        int points = 0;

        if (Boolean.TRUE.equals(prediction.getPredictedPodium())) {

            points += processDriver(
                    "1",
                    raceResult.getId(),
                    prediction.getFirst(),
                    raceResult.getFirst());

            points += processDriver(
                    "2",
                    raceResult.getId(),
                    prediction.getSecond(),
                    raceResult.getSecond());

            points += processDriver(
                    "3",
                    raceResult.getId(),
                    prediction.getThird(),
                    raceResult.getThird());
        }

        if (Boolean.TRUE.equals(prediction.getPredictedFastestLap())) {

            int fastestLapPoints =
                    Objects.equals(prediction.getFastestLap(), raceResult.getFastestLap())
                            ? 5
                            : 0;

            createDriversPoints(
                    prediction.getFastestLap(),
                    raceResult.getId(),
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


    public int getSeasonRaces(Integer season) {
        List<RaceResult> racesBySeason = raceResultRepository.findBySeason(season);
        return racesBySeason.size();
    }


    public RaceResult getRacePassed() {
        return raceResultRepository.findTopByRaceFinishedTrueOrderByRoundDesc();
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

    public List<PointsInfoDto> getPointsInfo(Integer userId, String round) throws JsonProcessingException {

        RaceResult raceResult = raceResultRepository.findByRound(Integer.parseInt(round));

        if (raceResult == null) {
            return Collections.emptyList();
        }

        Prediction prediction = predictRepository
                .findByUserIdAndRound(userId, Integer.parseInt(round))
                .orElse(null);

        if (prediction == null) {
            return Collections.emptyList();
        }

        List<PredictionResult> predictionResults =
                predictionResultRepository.findByPredictionId(String.valueOf(prediction.getId()));

        if (!predictionResults.isEmpty()) {
            PredictionResult predictionResult = predictionResults.get(0);
            predictionResult.setShowPointsUser(false);
            predictionResultRepository.save(predictionResult);
        }

        List<String> drivers = Stream.of(
                        prediction.getFirst(),
                        prediction.getSecond(),
                        prediction.getThird(),
                        prediction.getFastestLap())
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
}
