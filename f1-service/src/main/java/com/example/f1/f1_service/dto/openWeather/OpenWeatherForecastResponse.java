package com.example.f1.f1_service.dto.openWeather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenWeatherForecastResponse(
        List<ForecastItem> list
) {
    public record ForecastItem(@JsonProperty("dt_txt")
                               String dateTime,
                               List<OpenWeatherDescription> weather,

                               OpenWeatherMain main,

                               OpenWeatherWind wind) {

    }
}
