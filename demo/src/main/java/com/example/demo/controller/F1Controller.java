package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.demo.model.Results;
import com.example.demo.service.ErgastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
public class F1Controller {


	@Autowired
	private ErgastService ergastService;
	
	
	 public F1Controller(RestTemplateBuilder restTemplateBuilder) {
		  }
	 
	 @GetMapping("/teste")
	 public String teste() {
		 return "testing docker";
	 }
	 
	 @GetMapping("/testeV2")
	 public String teste2() {
		 return "testing docker Versao 2";
	 }
	  
	  @GetMapping("/{season}")
	  public List<Race> getRaces(@PathVariable String season) throws JsonProcessingException {
	    return ergastService.getRaces(season);
	  }
	  
	  
	  @GetMapping("/rawData/{season}")
	  public List<Driver> rawDataDrivers(@PathVariable String season) throws JsonProcessingException {
		  return ergastService.rawData(season, "");
	  }
	  
	  
	  @GetMapping("/race/{season}")
	  public ArrayList<String> getRaceName(@PathVariable String season) throws JsonProcessingException {
	    return ergastService.getNameRaces(season);
	  }
	  
	  @GetMapping("/raceResult/{season}/{round}")
	  public List<Results> getRaceResult(@PathVariable String season, @PathVariable String round) throws JsonMappingException, JsonProcessingException {
		  return ergastService.getRaceResult(season,round);
	  }
	  
	  
}