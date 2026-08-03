package com.example.demo.model;

import com.example.utils.LocalDateDeserializer;
import com.example.utils.LocalTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
import java.time.LocalTime;

public class SprintShootout {
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime time;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
