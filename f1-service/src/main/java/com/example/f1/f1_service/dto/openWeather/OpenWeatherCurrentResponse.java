package com.example.f1.f1_service.dto.openWeather;

import java.util.List;

public record OpenWeatherCurrentResponse(List<OpenWeatherDescription> weather,
                                         OpenWeatherMain main,
                                         OpenWeatherWind wind) {
}
