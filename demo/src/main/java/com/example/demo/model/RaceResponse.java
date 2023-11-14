package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RaceResponse {
	
	
	@JsonProperty("MRData")
	private MrData mrData;

	@JsonProperty("Race")
	List<Race> races;


	public List<Race> getRaces() {
		return races;
	}

	public void setRaces(List<Race> races) {
		this.races = races;
	}

	public MrData getMrData() {
		return mrData;
	}

	public void setMrData(MrData mrData) {
		this.mrData = mrData;
	}


}
