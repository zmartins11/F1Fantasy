package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConstructorTable {

	private String season;
	private String driverId;
	@JsonProperty("Constructors")
	private List<Constructor> constructors;
	
	
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	public List<Constructor> getConstructors() {
		return constructors;
	}
	public void setConstructors(List<Constructor> constructors) {
		this.constructors = constructors;
	}
	
	
}
