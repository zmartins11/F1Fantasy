package com.example.f1.f1_service.controller;

import java.time.Year;
import java.util.List;


import com.example.f1.f1_service.dto.RaceInfo;
import com.example.f1.f1_service.dto.StandingsDto;
import com.example.f1.f1_service.model.Driver;
import com.example.f1.f1_service.model.Race;
import com.example.f1.f1_service.model.RaceResultDto;
import com.example.f1.f1_service.service.ErgastService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;



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

	@GetMapping("/raceResult/{season}/{round}")
	public ResponseEntity<RaceResultDto> getRaceResult(@PathVariable String season, @PathVariable String round)
			throws JsonProcessingException {
		return ResponseEntity.ok(ergastService.getRaceResult(season, round));
	}

	@GetMapping("/drivers/{season}")
	public ResponseEntity<List<Driver>> rawDataDrivers(@PathVariable String season) throws JsonProcessingException {
		return ResponseEntity.ok(ergastService.getDriversInSeason(season));
	}

	@GetMapping("/standings")
	public ResponseEntity<StandingsDto> standingsSeason() throws JsonProcessingException {
		return ResponseEntity.ok(ergastService.getStandings());
	}

	@GetMapping("/nextRaceDetails")
	public ResponseEntity<Race> getNextRaceDetails() throws JsonProcessingException {
		return ResponseEntity.ok(ergastService.getNextRace());
	}

	@GetMapping("/allRaces")
	public ResponseEntity<List<RaceInfo>> getAllRaces() throws JsonProcessingException {
		return ResponseEntity.ok(ergastService.getAllRaces(Year.now().toString()));
	}
}