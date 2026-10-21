package com.example.f1.f1_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {

	String lat;
	@JsonProperty("long")
	String longi;
	String locality;
	String country;


	
	
	
	
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLongi() {
		return longi;
	}

	public void setLongi(String longi) {
		this.longi = longi;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
