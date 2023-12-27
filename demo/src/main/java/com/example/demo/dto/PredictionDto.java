package com.example.demo.dto;

import lombok.Data;

@Data
public class PredictionDto {

    private String user;
    private String first;
    private String second;
    private String third;
    private String round;
    private String fastestLap;
    private Boolean predictedPodium;
    private Boolean predictedFastestLap;
}
