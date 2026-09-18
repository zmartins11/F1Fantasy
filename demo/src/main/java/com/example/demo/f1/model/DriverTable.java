package com.example.demo.f1.model;

import java.util.List;

import com.example.demo.f1.model.Driver;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DriverTable {

	private String season;
	
	@JsonProperty("Drivers")
	private List<Driver> drivers;
	
	
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public List<Driver> getDrivers() {
		return drivers;
	}
	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}
	
	
	
	
	
}
