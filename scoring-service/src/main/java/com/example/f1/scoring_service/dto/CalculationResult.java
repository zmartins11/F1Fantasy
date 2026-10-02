package com.example.f1.scoring_service.dto;

import java.util.List;

public record CalculationResult(int totalPoints,
                                List<DriverPointsData> driverPoints) {
}
