package com.example.demo.f1.adapter;

import com.example.demo.f1.model.Driver;
import com.example.demo.f1.service.ErgastService;
import com.example.demo.scoring.port.DriverData;
import com.example.demo.scoring.port.DriverReader;
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
