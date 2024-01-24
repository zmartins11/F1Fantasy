package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class NextRaceInfoDto {

    private String nameRace;
    private String time;
    private String round;
    private String country;
    private String city;
    private LocalDate raceDate;
    private LocalTime raceTime;
    private Boolean predictionLocked;
    private Boolean userHavePrediction;
    private String first;
    private String second;
    private String third;
    private String fastestLap;
    private Boolean predictedPodium;
    private Boolean predictedFastestLap;
}
