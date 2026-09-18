package com.example.demo.f1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FastestLap {

	@JsonProperty("Time")
	private TimeResult time;
	private String averageSpeed;
	private String rank;

	private String lap;


	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public TimeResult getTime() {
		return time;
	}

	public void setTime(TimeResult time) {
		this.time = time;
	}

	public String getAverageSpeed() {
		return averageSpeed;
	}
	public void setAverageSpeed(String averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public String getLap() {
		return lap;
	}

	public void setLap(String lap) {
		this.lap = lap;
	}
}
