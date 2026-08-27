package com.example.demo.model.fantasy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
public class RaceResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "round")
    private Integer round;
    private String circuit;
    @Column(name = "season")
    private Integer season;
    private String first;
    private String second;
    private String third;
    private String fastestLap;
    private boolean raceFinished;
    private boolean predictionLocked;
    private boolean pointsCalculated;
}
