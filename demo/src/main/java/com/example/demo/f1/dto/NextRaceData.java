package com.example.demo.f1.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record NextRaceData(
        String nameRace,
        String time,
        String round,
        String country,
        String city,
        LocalDate raceDate,
        LocalTime raceTime,
        Boolean predictionLocked
) {
}
