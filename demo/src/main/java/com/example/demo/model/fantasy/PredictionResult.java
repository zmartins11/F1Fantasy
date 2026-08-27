package com.example.demo.model.fantasy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
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
