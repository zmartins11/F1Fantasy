package com.example.f1.f1_service.service;

import com.example.f1.f1_service.client.OpenWeatherClient;
import com.example.f1.f1_service.dto.WeatherResponse;
import com.example.f1.f1_service.dto.openWeather.OpenWeatherCurrentResponse;
import com.example.f1.f1_service.dto.openWeather.OpenWeatherForecastResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WeatherService {

    private final OpenWeatherClient openWeatherClient;

    public WeatherService(OpenWeatherClient openWeatherClient) {
        this.openWeatherClient = openWeatherClient;
    }

    public List<WeatherResponse> getRaceWeather(String country, String city, Integer hour, Integer day, Integer month, boolean getForecastForWeek) {

        String resolvedCity = city;

        OpenWeatherForecastResponse weatherData;

        try {
            weatherData = openWeatherClient.getForecast(city);

        } catch (HttpClientErrorException.NotFound e) {

            log.warn("City '{}' not found. Trying country '{}'", city, country);

            resolvedCity = country;
            weatherData = openWeatherClient.getForecast(resolvedCity);
        }

        List<WeatherResponse> forecast = new ArrayList<>();

        DateTimeFormatter openWeatherFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (OpenWeatherForecastResponse.ForecastItem item : weatherData.list()) {

            LocalDateTime date =
                    LocalDateTime.parse(item.dateTime(), openWeatherFormatter);

            WeatherResponse forecastItem = new WeatherResponse(
                    date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    item.weather().get(0).description(),
                    item.main().temp(),
                    item.main().humidity(),
                    item.wind().speed(),
                    false
            );

            if (!getForecastForWeek) {

                if (hour != null && day != null && month != null
                        && date.getHour() == hour
                        && date.getDayOfMonth() == day
                        && date.getMonthValue() == month) {

                    forecast.add(forecastItem);
                }

            } else {

                if (date.getHour() == 12) {
                    forecast.add(forecastItem);
                }
            }
        }

        /*
         * Mesmo comportamento do Python:
         * se não encontrarmos forecast para a data,
         * devolvemos o tempo atual.
         */
        if (forecast.isEmpty()) {

            OpenWeatherCurrentResponse current =
                    openWeatherClient.getCurrentWeather(resolvedCity);

            WeatherResponse currentWeather = new WeatherResponse(
                    LocalDateTime.now()
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),

                    current.weather().get(0).description(),

                    current.main().temp(),

                    current.main().humidity(),

                    current.wind().speed(),

                    true
            );
            return List.of(currentWeather);
        }
        return forecast;
    }
}
