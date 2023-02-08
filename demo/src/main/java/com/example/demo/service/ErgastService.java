package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Driver;
import com.example.demo.model.DriverResponse;
import com.example.demo.model.Race;
import com.example.demo.model.RaceResponse;
import com.example.demo.model.RaceTable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ErgastService {

	private final RestTemplate restTemplate = new RestTemplate();

	public List<Race> getRaces(String season) throws JsonProcessingException {
		String url = "http://ergast.com/api/f1/" + season + "/races.json";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		RaceResponse racesResponse = mapper.readValue(response.getBody(), RaceResponse.class);
		return racesResponse.getMrData().getRaceTable().getRace();

	}
	
	public List<Driver> rawData(String season, String round) throws JsonProcessingException {
		
		List<Driver> driversInSeason = null;
		
		String url = "http://ergast.com/api/f1/" + season + "/races.json";
		String urlRound = "http://ergast.com/api/f1/" + season + "/" + round +  "/races.json"; 
		String urlDriversByYear = "http://ergast.com/api/f1/" + season + "/drivers.json";
		
		ResponseEntity<String> response = restTemplate.getForEntity(urlDriversByYear, String.class);
		
		
		ObjectMapper mapper = new ObjectMapper();
		DriverResponse driverResponse = mapper.readValue(response.getBody(), DriverResponse.class);
		
		driversInSeason = driverResponse.getMrData().getDriverTable().getDrivers();
		
		return driversInSeason;
		
		

	}
	
	public ArrayList<String> getNameRaces(String season) throws JsonMappingException, JsonProcessingException {
		ArrayList<String> names = new ArrayList<>();
		
		String url = "http://ergast.com/api/f1/" + season + "/races.json";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		RaceResponse racesResponse = mapper.readValue(response.getBody(), RaceResponse.class);
		List<Race> races = racesResponse.getMrData().getRaceTable().getRace();
		for(Race r :races) {
			names.add(r.getRaceName());
		}
		
		return names;
	}
}
