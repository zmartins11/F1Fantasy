package com.example.demo.dto;

import lombok.Data;

@Data
public class PredictionDto {

    private Integer userId;
    private String first;
    private String second;
    private String third;
    private Integer round;
    private String fastestLap;
    private Boolean predictedPodium;
    private Boolean predictedFastestLap;
    private Integer season;
}
