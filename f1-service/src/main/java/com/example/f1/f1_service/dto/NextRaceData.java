package com.example.f1.f1_service.dto;

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
