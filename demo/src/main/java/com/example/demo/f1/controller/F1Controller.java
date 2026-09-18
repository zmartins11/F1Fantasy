package com.example.demo.f1.controller;

import java.time.Year;
import java.util.List;

import com.example.demo.f1.dto.RaceInfo;
import com.example.demo.f1.dto.StandingsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.f1.service.ErgastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.demo.f1.model.Race;
import com.example.demo.f1.model.RaceResultDto;


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