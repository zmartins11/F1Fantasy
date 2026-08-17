package com.example.demo.model.fantasy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PredictionResult {

    @Id
    @GeneratedValue()
    private Integer id;
    private String userId;
    private Integer points;
    private String predictionId;
    private Boolean showPointsUser;
    private String season;
    private Integer round;
}
