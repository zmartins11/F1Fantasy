package com.example.cronJob;

import com.example.demo.model.RaceResponse;
import com.example.demo.model.fantasy.RaceResult;
import com.example.demo.repository.RaceResultRepository;
import com.example.demo.service.ErgastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleRaceResult {

    // task runns every monday / tuesday
    // para correr api : round (vou buscar à tabela race_result : select * from table where finished = false)
    //                  season (vou buscar à tabela)
    // if mrData.getRaces is null (corrida ainda não terminada)
    // deste component passar o resultado para o predictService para guardar results.
    // no fim desta operacao fazer calculo dos pontos

    @Autowired
    private RaceResultRepository raceResultRepository;
    @Autowired
    private ErgastService ergastService;

    //correr a cada domingo
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void populateRaceResult() throws JsonProcessingException {
        List<RaceResult> racesNotFinished = raceResultRepository.findByRaceFinishedFalse();
        if (racesNotFinished != null) {
            for (RaceResult raceResult : racesNotFinished) {
                try {
                    RaceResult raceResultTemp = ergastService.getRaceResult(raceResult.getSeason(), raceResult.getRound());
                    if (raceResultTemp != null) {
                        raceResult.setRaceFinished(true);
                        raceResult.setFirst(raceResultTemp.getFirst());
                        raceResult.setSecond(raceResultTemp.getSecond());
                        raceResult.setThird(raceResultTemp.getThird());
                        raceResultRepository.save(raceResult);
                    }
                } catch (Exception e) {
                    System.out.print("race not finished!");
                }

            }
        }

    }
}
