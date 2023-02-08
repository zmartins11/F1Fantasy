package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RaceTable {

	
	@JsonProperty("Races")
	private List<Race> races;
	  
	 private String season;
	 
	 

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

	
}
