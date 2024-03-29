package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MrData {

	private String xmlns;
	private String series;
	private String url;
	private int limit;
	private int offset;
	private int total;

	@JsonProperty("StandingsTable")
	private StandingsTable StandingsTable;
	
	@JsonProperty("RaceTable")
	private RaceTable raceTable;

	private DriverTable driverTable;

	private ConstructorTable constructorTable; 

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public RaceTable getRaceTable() {
		return raceTable;
	}

	public void setRaceTable(RaceTable raceTable) {
		this.raceTable = raceTable;
	}

	public DriverTable getDriverTable() {
		return driverTable;
	}

	public void setDriverTable(DriverTable driverTable) {
		this.driverTable = driverTable;
	}

	public ConstructorTable getConstructorTable() {
		return constructorTable;
	}

	public void setConstructorTable(ConstructorTable constructorTable) {
		this.constructorTable = constructorTable;
	}

	public StandingsTable getStandingsTable() {
		return StandingsTable;
	}

	public void setStandingsTable(StandingsTable standingsTable) {
		StandingsTable = standingsTable;
	}
	
}
