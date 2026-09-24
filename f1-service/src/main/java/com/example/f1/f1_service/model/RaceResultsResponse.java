package com.example.f1.f1_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RaceResultsResponse {

	@JsonProperty("MRData")
	private MrData mrData;
	
	private RaceTable raceTable;

	public MrData getMrData() {
		return mrData;
	}

	public void setMrData(MrData mrData) {
		this.mrData = mrData;
	}

	public RaceTable getRaceTable() {
		return raceTable;
	}

	public void setRaceTable(RaceTable raceTable) {
		this.raceTable = raceTable;
	}
	
	
	
}
