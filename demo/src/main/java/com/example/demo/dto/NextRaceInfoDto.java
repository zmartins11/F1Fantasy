package com.example.demo.dto;

import lombok.Data;

@Data
public class NextRaceInfoDto {

    private String nameRace;
    private String time;
    private String round;
    private Boolean predictionLocked;
    private Boolean userHavePrediction;
    private String first;
    private String second;
    private String third;
    private String fastestLap;
    private Boolean predictedPodium;
    private Boolean predictedFastestLap;
}
