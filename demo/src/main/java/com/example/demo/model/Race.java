package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.utils.LocalDateDeserializer;
import com.example.utils.LocalDateSerializer;
import com.example.utils.LocalTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Race {

	private String season;
	private String round;
	private String url;
	private String raceName;
	
    @JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate date;
    
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime time;
    
	@JsonProperty("Circuit")
	private Circuit circuit;
	
	@JsonProperty("FirstPractice")
	private FirstPractice firstPractice;
	
	@JsonProperty("SecondPractice")
	private SecondPractice secondPractice;
	
	@JsonProperty("ThirdPractice")
	private ThirdPractice thirdPractice;
	
	@JsonProperty("Qualifying")
	private Qualifying qualifying;
	
	@JsonProperty("Sprint")
	private Sprint spring;
	
	public Qualifying getQualifying() {
		return qualifying;
	}

	public void setQualifying(Qualifying qualifying) {
		this.qualifying = qualifying;
	}

	public FirstPractice getFirstPractice() {
		return firstPractice;
	}

	public void setFirstPractice(FirstPractice firstPractice) {
		this.firstPractice = firstPractice;
	}

	public SecondPractice getSecondPractice() {
		return secondPractice;
	}

	public void setSecondPractice(SecondPractice secondPractice) {
		this.secondPractice = secondPractice;
	}

	public ThirdPractice getThirdPractice() {
		return thirdPractice;
	}

	public void setThirdPractice(ThirdPractice thirdPractice) {
		this.thirdPractice = thirdPractice;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getRaceName() {
		return raceName;
	}

	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Circuit getCircuit() {
		return circuit;
	}

	public void setCircuit(Circuit circuit) {
		this.circuit = circuit;
	}


	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
