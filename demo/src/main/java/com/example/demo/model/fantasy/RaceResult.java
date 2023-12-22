package com.example.demo.model.fantasy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
public class RaceResult {

    @Id
    @GeneratedValue()
    private Integer id;
    private Integer round;
    private String circuit;
    private String season;
    private String first;
    private String second;
    private String third;
    private String fastestLap;
    private boolean raceFinished;
    private boolean predictionLocked;

}
