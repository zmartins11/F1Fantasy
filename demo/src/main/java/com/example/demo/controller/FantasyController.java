package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.fantasy.Driverf;
import com.example.demo.model.fantasy.Fantasyteam;
import com.example.demo.model.fantasy.Constructorf;
import com.example.demo.service.FantasyService;

@RestController
@RequestMapping("fantasy")
public class FantasyController {
	
	@Autowired
	private FantasyService fantasyService;
	
	
	@PostMapping("/addTeam")
	public void addTeam(@RequestParam int d1,@RequestParam int d2,@RequestParam int d3,@RequestParam int team,@RequestParam int user) {
		fantasyService.addTeam(d1, d2, d3, team, user);
	}
	
	@GetMapping("/drivers")
	public List<Driverf> getAllDrivers() {
		return fantasyService.getAllDrivers();
	}
	
	@GetMapping("/teams")
	public List<Constructorf> getAllTeams() {
		return fantasyService.getAllTeams();
	}
	
	@GetMapping("/fantasyTeam/{userId}")
	public ResponseEntity<Optional<Fantasyteam>> getFantasyTeam(@PathVariable int userId) {
		Optional<Fantasyteam> fTeam = fantasyService.getFantasyTeam(userId);
		return ResponseEntity.ok(fTeam);
	}
	
	@PostMapping("/update")
	public ResponseEntity<Fantasyteam> updateTeam(@RequestBody Fantasyteam team) throws Exception {
		 Fantasyteam updatedTeam = fantasyService.updateTeam(team);
		 
		  if (updatedTeam != null) {
	            return ResponseEntity.ok(updatedTeam);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
	}
	

}
