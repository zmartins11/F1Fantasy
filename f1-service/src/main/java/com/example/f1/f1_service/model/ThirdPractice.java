package com.example.f1.f1_service.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.f1.f1_service.utils.LocalDateDeserializer;
import com.example.f1.f1_service.utils.LocalTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ThirdPractice {
	
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
