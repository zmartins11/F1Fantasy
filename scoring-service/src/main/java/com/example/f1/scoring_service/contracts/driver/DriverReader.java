package com.example.f1.scoring_service.contracts.driver;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface DriverReader {
    List<DriverData> findBySeason(String season) throws JsonProcessingException;
}
