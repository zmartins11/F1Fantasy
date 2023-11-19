package com.example.demo.dto;

import lombok.Data;

@Data
public class NextRaceInfoDto {

    private String nameRace;
    private String time;
    private String round;
    private Boolean predictionLocked;

}
