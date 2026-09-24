package com.example.f1.f1_service.adapter;

import com.example.f1.f1_service.contracts.driver.DriverData;
import com.example.f1.f1_service.contracts.driver.DriverReader;
import com.example.f1.f1_service.model.Driver;
import com.example.f1.f1_service.service.ErgastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DriverReaderAdapter implements DriverReader {

    private final ErgastService ergastService;

    public DriverReaderAdapter(ErgastService ergastService) {
        this.ergastService = ergastService;
    }

    @Override
    public List<DriverData> findBySeason(String season) throws JsonProcessingException {
        return ergastService.getDriversInSeason(season)
                .stream()
                .map(this::toData)
                .toList();
    }

    private DriverData toData(Driver driver) {
        return new DriverData(driver.getPermanentNumber(), driver.getFamilyName());
    }
}
