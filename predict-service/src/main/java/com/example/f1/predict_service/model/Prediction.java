package com.example.f1.predict_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "round")
    private Integer round;
    private String first;
    private String second;
    private String third;
    private String fastestLap;
    private Boolean predictedPodium;
    private Boolean predictedFastestLap;
    @Column(name = "season")
    private Integer season;
}
