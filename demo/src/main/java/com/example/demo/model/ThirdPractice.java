package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.utils.LocalDateDeserializer;
import com.example.utils.LocalDateSerializer;
import com.example.utils.LocalTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
