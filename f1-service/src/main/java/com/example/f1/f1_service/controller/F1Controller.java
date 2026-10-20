package com.example.f1.f1_service.controller;

import java.util.List;


import com.example.f1.f1_service.dto.NewsDto;
import com.example.f1.f1_service.dto.RaceInfo;
import com.example.f1.f1_service.dto.StandingsDto;
import com.example.f1.f1_service.enums.DriverDisplayType;
import com.example.f1.f1_service.model.Driver;
import com.example.f1.f1_service.model.Race;
import com.example.f1.f1_service.model.RaceResultDto;
import com.example.f1.f1_service.service.ErgastService;
import com.example.f1.f1_service.service.F1NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;



@RestController
@RequestMapping("/api/f1")
public class F1Controller {

	private final ErgastService ergastService;

	private final F1NewsService f1NewsService;

	public F1Controller(ErgastService ergastService, F1NewsService f1NewsService) {
		this.ergastService = ergastService;
		this.f1NewsService = f1NewsService;
	}

	@GetMapping("/{season}")
	public ResponseEntity<List<Race>> getRaces(@PathVariable String season) throws JsonProcessingException {
		return ResponseEntity.ok(ergastService.getRaces(season));
	}

	@GetMapping("/raceResult/{season}/{round}")
	public ResponseEntity<RaceResultDto> getRaceResult(@PathVariable String season, @PathVariable String round)
			throws JsonProcessingException {
		return ResponseEntity.ok(ergastService.getRaceResult(season, round, DriverDisplayType.FAMILY_NAME));
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

	@GetMapping("/allRaces/{season}")
	public ResponseEntity<List<RaceInfo>> getAllRaces(@PathVariable int season) throws JsonProcessingException {
		return ResponseEntity.ok(ergastService.getAllRaces(season));
	}

	@GetMapping("/news")
	public ResponseEntity<List<NewsDto>> getLatestNews() throws Exception {
		return ResponseEntity.ok(f1NewsService.getLatestNews());
	}
}