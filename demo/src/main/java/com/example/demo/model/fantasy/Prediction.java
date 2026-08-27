package com.example.demo.model.fantasy;

import com.example.demo.dto.PredictionDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
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
