package com.example.f1.f1_service.controller;


import com.example.f1.f1_service.dto.WeatherResponse;
import com.example.f1.f1_service.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/f1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public List<WeatherResponse> getWeather(
            @RequestParam String country,
            @RequestParam(defaultValue = "Lisbon") String city,
            @RequestParam(required = false) Integer hour,
            @RequestParam(required = false) Integer day,
            @RequestParam(required = false) Integer month,
            @RequestParam(defaultValue = "false") boolean forecast) {

        return weatherService.getRaceWeather(
                country,
                city,
                hour,
                day,
                month,
                forecast
        );
    }
}
