package com.example.f1.scoring_service.adapter;

import com.example.f1.scoring_service.contracts.driver.DriverData;
import com.example.f1.scoring_service.contracts.driver.DriverReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class F1DriverRestReader implements DriverReader {


    @Override
    public List<DriverData> findBySeason(String season) throws JsonProcessingException {
        return null;
    }
}
