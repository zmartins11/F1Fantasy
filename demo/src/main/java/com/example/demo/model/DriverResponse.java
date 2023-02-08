package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DriverResponse {

	@JsonProperty("MRData")
	private MrData mrData;
	
	@JsonProperty("Driver")
	List<Driver> drivers;

	public MrData getMrData() {
		return mrData;
	}

	public void setMrData(MrData mrData) {
		this.mrData = mrData;
	}

	public List<Driver> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}
	
	
	
	
}
