package com.example.demo.scoring.service;

import com.example.demo.auth.port.UserData;
import com.example.demo.auth.port.UserReader;
import com.example.demo.f1.model.entity.RaceResult;
import com.example.demo.f1.service.RaceResultService;
import com.example.demo.prediction.model.Prediction;
import com.example.demo.prediction.repository.PredictRepository;
import com.example.demo.scoring.model.DriversPoints;
import com.example.demo.scoring.model.PredictionResult;
import com.example.demo.scoring.repository.DriversPointsRepository;
import com.example.demo.scoring.repository.PredictionResultRepository;
import com.example.demo.scoring.port.PredictionData;
import com.example.demo.scoring.port.PredictionReader;
import com.example.demo.contracts.race.RaceFinishedEvent;
import com.example.demo.contracts.race.RaceResultData;
import com.example.demo.scoring.port.RaceResultReader;
import com.example.demo.scoring.port.RaceResultWriter;
import com.example.demo.contracts.driver.DriverData;
import com.example.demo.contracts.driver.DriverReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {

    @Mock
    private PredictionResultRepository predictionResultRepository;

    @Mock
    private DriversPointsRepository driversPointsRepository;

    @Mock
    private PredictRepository predictRepository;

    @Mock
    private RaceResultService raceResultService;

    @Mock
    private UserReader userReader;

    @Mock
    private DriverReader driverReader;

    @Mock
    private PredictionReader predictionReader;

    @Mock
    private RaceResultReader raceResultReader;

    @Mock
    private RaceResultWriter raceResultWriter;

    @InjectMocks
    private ScoringService scoringService;

    @Test
    void shouldCalculateAndSavePodiumAndFastestLapPoints() {
        RaceFinishedEvent event = raceFinishedEvent();

        when(raceResultReader.findBySeasonAndRound(2024, 5)).thenReturn(raceResultData());
        when(predictionReader.findBySeasonAndRound(2024, 5)).thenReturn(List.of(predictionData()));
        when(predictionResultRepository.findByPredictionId("10")).thenReturn(List.of());
        when(driversPointsRepository.findByRaceIdAndDriverAndPosition("1", "44", "1"))
                .thenReturn(List.of());
        when(driversPointsRepository.findByRaceIdAndDriverAndPosition("1", "33", "2"))
                .thenReturn(List.of());
        when(driversPointsRepository.findByRaceIdAndDriverAndPosition("1", "16", "3"))
                .thenReturn(List.of());
        when(driversPointsRepository.findByRaceIdAndDriverAndPosition("1", "44", "fastestLap"))
                .thenReturn(List.of());

        scoringService.calculateAndSavePoints(event);

        ArgumentCaptor<PredictionResult> resultCaptor = ArgumentCaptor.forClass(PredictionResult.class);
        verify(predictionResultRepository).save(resultCaptor.capture());
        assertEquals(20, resultCaptor.getValue().getPoints());
        assertEquals(42, resultCaptor.getValue().getUserId());
        assertEquals("10", resultCaptor.getValue().getPredictionId());
        assertEquals(Boolean.TRUE, resultCaptor.getValue().getShowPointsUser());

        verify(driversPointsRepository, org.mockito.Mockito.times(4))
                .save(org.mockito.ArgumentMatchers.any(DriversPoints.class));
        verify(raceResultWriter).markPointsCalculated(1);
    }

    @Test
    void shouldNotRecalculatePredictionWithExistingResult() {
        RaceFinishedEvent event = raceFinishedEvent();
        PredictionResult existingResult = new PredictionResult();

        when(raceResultReader.findBySeasonAndRound(2024, 5)).thenReturn(raceResultData());
        when(predictionReader.findBySeasonAndRound(2024, 5)).thenReturn(List.of(predictionData()));
        when(predictionResultRepository.findByPredictionId("10")).thenReturn(List.of(existingResult));

        scoringService.calculateAndSavePoints(event);

        verify(predictionResultRepository, org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any(PredictionResult.class));
        verifyNoInteractions(driversPointsRepository);
        verify(raceResultWriter).markPointsCalculated(1);
    }

    @Test
    void shouldReturnPointsInfoAndHideDisplayedResult() throws Exception {
        RaceResult raceResult = raceResult(2024, 5);
        raceResult.setId(1);
        Prediction prediction = prediction(10, 42, 2024, 5);
        PredictionResult predictionResult = new PredictionResult();
        DriversPoints driversPoints = new DriversPoints();
        driversPoints.setDriver("44");
        driversPoints.setPoints(5);
        driversPoints.setPosition("1");
        when(raceResultReader.findByRound(5)).thenReturn(raceResultData());
        when(predictionReader.findByUserIdAndRound(42, 5)).thenReturn(predictionData());
        when(predictionResultRepository.findByPredictionId("10")).thenReturn(List.of(predictionResult));
        when(driversPointsRepository.findByDriverInAndRaceId(
                List.of("44", "33", "16", "44"), "1"))
                .thenReturn(List.of(driversPoints));
        when(driverReader.findBySeason("2024"))
            .thenReturn(List.of(new DriverData(44, "Hamilton")));

        var result = scoringService.getPointsInfo(42, "5");

        assertEquals(1, result.size());
        assertEquals("44", result.get(0).getDriver());
        assertEquals("Hamilton", result.get(0).getFamilyName());
        assertEquals(5, result.get(0).getPoints());
        verify(predictionResultRepository).save(predictionResult);
    }

    @Test
    void shouldReturnUsersOrderedByTotalPoints() {
        UserData firstUser = user(1, "first");
        UserData secondUser = user(2, "second");
        PredictionResult firstResult = new PredictionResult();

        when(userReader.findAll()).thenReturn(List.of(firstUser, secondUser));
        when(predictionResultRepository.findByUserId(1)).thenReturn(List.of(firstResult));
        when(predictionResultRepository.findByUserId(2)).thenReturn(List.of());
        when(predictionResultRepository.sumPointsByUserId(1)).thenReturn(25L);

        var result = scoringService.getTotalPoints();

        assertEquals(2, result.size());
        assertEquals("first", result.get(0).getUsername());
        assertEquals("25", result.get(0).getPoints());
        assertEquals("1", result.get(0).getPosition());
        assertEquals("second", result.get(1).getUsername());
        assertEquals("0", result.get(1).getPoints());
        assertEquals("2", result.get(1).getPosition());
    }

    private RaceResult raceResult(Integer season, Integer round) {
        RaceResult raceResult = new RaceResult();
        raceResult.setId(1);
        raceResult.setSeason(season);
        raceResult.setRound(round);
        raceResult.setFirst("44");
        raceResult.setSecond("33");
        raceResult.setThird("16");
        raceResult.setFastestLap("44");
        return raceResult;
    }

    private Prediction prediction(Integer id, Integer userId, Integer season, Integer round) {
        Prediction prediction = new Prediction();
        prediction.setId(id);
        prediction.setUserId(userId);
        prediction.setSeason(season);
        prediction.setRound(round);
        prediction.setFirst("44");
        prediction.setSecond("33");
        prediction.setThird("16");
        prediction.setFastestLap("44");
        prediction.setPredictedPodium(true);
        prediction.setPredictedFastestLap(true);
        return prediction;
    }

    private PredictionData predictionData() {
        return new PredictionData(10, 42, 2024, 5,
                "44", "33", "16", "44", true, true);
    }

    private RaceResultData raceResultData() {
        return new RaceResultData(1, 2024, 5,
                "44", "33", "16", "44");
    }

    private RaceFinishedEvent raceFinishedEvent() {
        return new RaceFinishedEvent(1, 2024, 5,
                "44", "33", "16", "44");
    }

    private UserData user(Integer id, String username) {
        return new UserData(id, username, null, null);
    }
}
