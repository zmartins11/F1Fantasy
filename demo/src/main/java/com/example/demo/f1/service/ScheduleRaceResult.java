package com.example.demo.f1.service;

import com.example.demo.f1.model.RaceResultDto;
import com.example.demo.f1.model.RaceResult;
import com.example.demo.scoring.port.RaceResultData;
import com.example.demo.scoring.service.ScoringService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Component
@Slf4j
public class ScheduleRaceResult {

    private final RaceResultService raceResultService;
    private final ErgastService ergastService;
    private final ScoringService scoringService;

    public ScheduleRaceResult(RaceResultService raceResultService,
                              ErgastService ergastService,
                              ScoringService scoringService) {

        this.raceResultService = raceResultService;
        this.ergastService = ergastService;
        this.scoringService = scoringService;
    }


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
            RaceResult currentRace = raceResultService.findBySeasonAndRound(season, round);

            if (currentRace != null && currentRace.isPointsCalculated()) {
                log.debug("Skipping already processed race {}-{}", season, round);
                return;
            }

            if (currentRace == null) {
                currentRace = updateRaceResult(lastFinishedRaceResult, season, round);
            }

            scoringService.calculateAndSavePoints(toData(currentRace));
        } catch (RestClientException exception) {
            log.warn("Could not retrieve the latest race result; it will be retried", exception);
        } catch (JsonProcessingException exception) {
            log.error("Could not parse the latest race result", exception);
        } catch (RuntimeException exception) {
            log.error("Unexpected error processing the latest race result", exception);
        }
    }

    private RaceResultData toData(RaceResult raceResult) {
        if (raceResult == null) {
            return null;
        }
        return new RaceResultData(raceResult.getId(),
                raceResult.getSeason(),
                raceResult.getRound(),
                raceResult.getFirst(),
                raceResult.getSecond(),
                raceResult.getThird(),
                raceResult.getFastestLap());
    }

    private RaceResult updateRaceResult(RaceResultDto apiRaceResult, Integer season, Integer round) {
        RaceResult raceResultLastRace = new RaceResult();
        raceResultLastRace.setSeason(season);
        raceResultLastRace.setRound(round);
        raceResultLastRace.setRaceFinished(true);
        raceResultLastRace.setFirst(apiRaceResult.getFirst());
        raceResultLastRace.setSecond(apiRaceResult.getSecond());
        raceResultLastRace.setThird(apiRaceResult.getThird());
        raceResultLastRace.setFastestLap(apiRaceResult.getFastestLap());

        return raceResultService.save(raceResultLastRace);
    }



}
