package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {

	String lat;
	@JsonProperty("long")
	String longi;
	String locality;
	String Country;


	
	
	
	
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
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

}
