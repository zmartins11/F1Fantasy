package com.example.f1.scoring_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(
        name = "drivers_points",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_driver_points_race_driver_position",
                columnNames = {
                        "race_id",
                        "driver",
                        "position"
                }
        )
)
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
