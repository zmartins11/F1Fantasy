package com.example.demo.scoring.port;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface DriverReader {
    List<DriverData> findBySeason(String season) throws JsonProcessingException;
}
