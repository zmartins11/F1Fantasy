package com.example.f1.scoring_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PredictionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    private Integer points;
    private String predictionId;
    private Boolean showPointsUser;
    @Column(name = "season")
    private Integer season;
    @Column(name = "round")
    private Integer round;
}
