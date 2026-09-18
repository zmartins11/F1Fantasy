package com.example.demo.f1.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.f1.utils.LocalDateDeserializer;
import com.example.demo.f1.utils.LocalTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Sprint {

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
