package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ErgastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.demo.model.Driver;
import com.example.demo.model.Race;
import com.example.demo.model.RaceResultDto;


@RestController
public class F1Controller {

	private final ErgastService ergastService;

	public F1Controller(ErgastService ergastService) {
		this.ergastService = ergastService;
	}

	@GetMapping("/{season}")
	public ResponseEntity<List<Race>> getRaces(@PathVariable String season) throws JsonProcessingException {
		return ResponseEntity.ok(ergastService.getRaces(season));
	}

	@GetMapping("/rawData/{season}")
	public ResponseEntity<List<Driver>> rawDataDrivers(@PathVariable String season) throws JsonProcessingException {
		return ResponseEntity.ok(ergastService.getDriversInSeason(season));
	}

	@GetMapping("/raceResult/{season}/{round}")
	public ResponseEntity<RaceResultDto> getRaceResult(@PathVariable String season, @PathVariable String round)
			throws JsonProcessingException {
		return ResponseEntity.ok(ergastService.getRaceResult(season, round));
	}
}