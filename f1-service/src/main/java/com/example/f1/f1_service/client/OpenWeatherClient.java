package com.example.f1.f1_service.client;

import com.example.f1.f1_service.dto.openWeather.OpenWeatherCurrentResponse;
import com.example.f1.f1_service.dto.openWeather.OpenWeatherForecastResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenWeatherClient {

    private final RestClient restClient;
    private final String apiKey;

    public OpenWeatherClient(
            @Value("${openweather.api-key}") String apiKey) {

        this.restClient = RestClient.builder()
                .baseUrl("https://api.openweathermap.org")
                .build();

        this.apiKey = apiKey;
    }

    public OpenWeatherForecastResponse getForecast(String city) {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/forecast")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .body(OpenWeatherForecastResponse.class);
    }

    public OpenWeatherCurrentResponse getCurrentWeather(String city) {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .body(OpenWeatherCurrentResponse.class);
    }


}
