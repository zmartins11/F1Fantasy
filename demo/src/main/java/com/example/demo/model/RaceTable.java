package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RaceTable {

	
	@JsonProperty("Races")
	private List<Race> races;
	  
	 private String season;
	 private String driverId;
	 private String position;
	 private String round;

	  public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public List<Race> getRace() {
		return races;
	}

	public void setRace(List<Race> race) {
		this.races = race;
	}

	public List<Race> getRaces() {
		return races;
	}

	public void setRaces(List<Race> races) {
		this.races = races;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}
	
	
	

	
}
