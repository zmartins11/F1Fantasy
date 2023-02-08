package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Circuit {

	private String circuitId;
	private String url;
	private String circuitName;
	
	@JsonProperty("Location")
	private Location location;
	
	
	public String getCircuitName() {
		return circuitName;
	}
	public void setCircuitName(String circuitName) {
		this.circuitName = circuitName;
	}

	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getCircuitId() {
		return circuitId;
	}
	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
