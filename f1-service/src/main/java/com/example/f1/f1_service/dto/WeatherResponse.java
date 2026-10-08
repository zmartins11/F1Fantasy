package com.example.f1.f1_service.dto;

public record WeatherResponse(String date,
                              String weather,
                              double temperature,
                              int humidity,
                              double windSpeed,
                              boolean currentWeather) {
}
