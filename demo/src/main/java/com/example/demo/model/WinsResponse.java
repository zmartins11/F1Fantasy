package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WinsResponse {

	@JsonProperty("MRData")
	private MrData mrData;


	public MrData getMrData() {
		return mrData;
	}

	public void setMrData(MrData mrData) {
		this.mrData = mrData;
	}

}
