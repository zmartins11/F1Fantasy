package com.example.demo.model.fantasy;

import com.example.demo.dto.PredictionDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Prediction {

    @Id
    @GeneratedValue()
    private Integer id;
    private String userId;
    private String raceId;
    private String round;
    private String first;
    private String second;
    private String third;
    private String fastestLap;
    private Boolean predictedPodium;
    private Boolean predictedFastestLap;

}
