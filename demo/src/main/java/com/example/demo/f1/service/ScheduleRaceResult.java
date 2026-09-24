package com.example.demo.f1.service;

import com.example.demo.contracts.race.RaceFinishedEvent;
import com.example.demo.f1.model.RaceResultDto;
import com.example.demo.f1.model.entity.RaceResult;
import com.example.demo.f1.publisher.RaceFinishedPublisher;
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

    private final RaceFinishedPublisher raceFinishedPublisher;


    public ScheduleRaceResult(RaceResultService raceResultService,
                              ErgastService ergastService, RaceFinishedPublisher raceFinishedPublisher) {

        this.raceResultService = raceResultService;
        this.ergastService = ergastService;
        this.raceFinishedPublisher = raceFinishedPublisher;
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

            RaceFinishedEvent event = new RaceFinishedEvent(
                    currentRace.getId(),
                    currentRace.getSeason(),
                    currentRace.getRound(),
                    currentRace.getFirst(),
                    currentRace.getSecond(),
                    currentRace.getThird(),
                    currentRace.getFastestLap()
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
