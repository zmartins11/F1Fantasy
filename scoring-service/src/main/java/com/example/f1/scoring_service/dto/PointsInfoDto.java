package com.example.f1.scoring_service.dto;

import lombok.Data;

@Data
public class PointsInfoDto {
    private String driver;
    private String familyName;
    private String position;
    private Integer points;
}
