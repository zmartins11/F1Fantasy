package com.example.f1.f1_service.service;

import com.example.f1.f1_service.contracts.race.RaceFinishedEvent;
import com.example.f1.f1_service.model.RaceResultDto;
import com.example.f1.f1_service.model.entity.RaceResult;
import com.example.f1.f1_service.publisher.RaceFinishedPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
@Slf4j
public class RaceResultProcessingService {

    private final ErgastService ergastService;
    private final RaceFinishedPublisher raceFinishedPublisher;
    private final RaceResultService raceResultService;

    public RaceResultProcessingService(ErgastService ergastService, RaceFinishedPublisher raceFinishedPublisher, RaceResultService raceResultService) {
        this.ergastService = ergastService;
        this.raceFinishedPublisher = raceFinishedPublisher;
        this.raceResultService = raceResultService;
    }

    public void processLatestFinishedRace() throws JsonProcessingException {

        try {
            log.info("SCHEDULED STARTED......");
            RaceResultDto lastFinishedRaceResult = ergastService.getLastFinishedRaceResult();
            if (lastFinishedRaceResult == null) {
                log.info("No finished race found");
                return;
            }

            Integer season = Integer.valueOf(lastFinishedRaceResult.getSeason());
            Integer round = lastFinishedRaceResult.getRound();
            RaceResult currentRace = raceResultService.findBySeasonAndRound(season, round);

            if (currentRace != null && currentRace.isPointsCalculated()) {
                log.info("Skipping already processed race {}-{}", season, round);
                return;
            }

            if (currentRace == null) {
                currentRace = updateRaceResult(lastFinishedRaceResult, season, round);
            }

            RaceFinishedEvent event = new RaceFinishedEvent(
                    currentRace.getId(),
                    currentRace.getSeason(),
                    currentRace.getRound(),
                    currentRace.getFirst(),
                    currentRace.getSecond(),
                    currentRace.getThird(),
                    currentRace.getFastestLap()
            );
            log.info(
                    "Publishing RaceFinishedEvent: raceId={}, season={}, round={}",
                    event.raceResultId(),
                    event.season(),
                    event.round()
            );

            raceFinishedPublisher.publish(event);
        } catch (RestClientException exception) {
            log.warn("Could not retrieve the latest race result; it will be retried", exception);
        } catch (JsonProcessingException exception) {
            log.error("Could not parse the latest race result", exception);
        } catch (RuntimeException exception) {
            log.error("Unexpected error processing the latest race result", exception);
        }
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
