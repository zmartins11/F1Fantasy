package com.example.demo.f1.model;

import lombok.Data;

@Data
public class RaceResultDto {
    private Integer round;
    private String circuit;
    private String season;
    private String first;
    private String second;
    private String third;
    private String fastestLap;
    private boolean raceFinished;
    private boolean predictionLocked;
}
