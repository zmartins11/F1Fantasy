package com.example.demo.model;

import java.time.LocalTime;

import com.example.utils.LocalTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Results {
	
	private String number;
	private String position;
	private String positionText;
	private String points;
	private String grid;
	private String laps;
	private String status;
	
	@JsonDeserialize(using = LocalTimeDeserializer.class)
	private LocalTime time;
	@JsonIgnore
	private FastesLap fastestLap;
	
	@JsonProperty("Driver")
	Driver driver;
	@JsonProperty("Constructor")
	Constructor constructor;
	
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getPositionText() {
		return positionText;
	}
	public void setPositionText(String positionText) {
		this.positionText = positionText;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	public Constructor getConstructor() {
		return constructor;
	}
	public void setConstructor(Constructor constructor) {
		this.constructor = constructor;
	}
	public String getGrid() {
		return grid;
	}
	public void setGrid(String grid) {
		this.grid = grid;
	}
	public String getLaps() {
		return laps;
	}
	public void setLaps(String laps) {
		this.laps = laps;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public FastesLap getFastestLap() {
		return fastestLap;
	}
	public void setFastestLap(FastesLap fastestLap) {
		this.fastestLap = fastestLap;
	}
	public LocalTime getTime() {
		return time;
	}
	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	

}
