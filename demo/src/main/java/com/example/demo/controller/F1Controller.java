package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Driver;
import com.example.demo.model.DriverResponse;
import com.example.demo.model.Race;
import com.example.demo.service.ErgastService;
import com.fasterxml.jackson.core.JsonProcessingException;


@RestController
public class F1Controller {

	
	private final ErgastService ergastService;
	
	private final RestTemplate restTemplate;
	
	 public F1Controller(RestTemplateBuilder restTemplateBuilder) {
		    this.ergastService = new ErgastService();
			this.restTemplate = restTemplateBuilder.build();
		  }
	  
	  @GetMapping("/{season}")
	  public List<Race> getRaces(@PathVariable String season) throws JsonProcessingException {
	    return ergastService.getRaces(season);
	  }
	  
//	  @GetMapping("/rawData/{season}")
//	  public ResponseEntity<String> rawData(@PathVariable String season, @PathVariable String round) throws JsonProcessingException {
//		  return ergastService.rawData(season, round);
//	  }
	  
	  @GetMapping("/rawData/{season}")
	  public List<Driver> rawDataDrivers(@PathVariable String season) throws JsonProcessingException {
		  return ergastService.rawData(season, "");
	  }
	  
	  
	  @GetMapping("/race/{season}")
	  public ArrayList<String> getRaceName(@PathVariable String season) throws JsonProcessingException {
	    return ergastService.getNameRaces(season);
	  }
	  
	  
}