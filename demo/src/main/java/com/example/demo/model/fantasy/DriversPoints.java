package com.example.demo.model.fantasy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class DriversPoints {
    @Id
    @GeneratedValue()
    private Integer id;
    private String driver;
    private Integer points;
    private Integer predictionId;
    private Integer raceId;
    private String position;
}
