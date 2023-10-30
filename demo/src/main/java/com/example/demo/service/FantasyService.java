package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.fantasy.Driverf;
import com.example.demo.model.fantasy.Fantasyteam;
import com.example.demo.model.fantasy.Constructorf;
import com.example.demo.repository.ConstructorFantasyRepository;
import com.example.demo.repository.DriverFantasyRepository;
import com.example.demo.repository.FantasyTeamJpaRepository;
import com.example.demo.repository.FantasyTeamRepository;

@Service
public class FantasyService {
	
	@Autowired
	private FantasyTeamRepository fantasyRepo;
	
	@Autowired
	private DriverFantasyRepository driversRepo;
	
	@Autowired
	private ConstructorFantasyRepository constructorRepo;
	
	@Autowired
	private FantasyTeamJpaRepository fantasyJpa;


	public void addTeam(int d1, int d2, int d3, int team, int user) {
		
		int af = fantasyRepo.saveFantasyTeam(d1, d2, d3, team, user);
	}
	
	public List<Driverf> getAllDrivers() {
		return driversRepo.findAll();
	}

	public List<Constructorf> getAllTeams() {
		return constructorRepo.findAll();
	}
	
	public Optional<Fantasyteam> getFantasyTeam(int userId) {
		Optional<Fantasyteam> team = fantasyJpa.findByUserId(userId);
		return team;
	}

	public Fantasyteam updateTeam(Fantasyteam team) throws Exception {
		Optional<Fantasyteam> optionalTeam = fantasyJpa.findById(team.getId());
		
		if(optionalTeam.isPresent()) {
			Fantasyteam existingTeam = optionalTeam.get();
			
			 existingTeam.setDriver1(team.getDriver1());
	         existingTeam.setDriver2(team.getDriver2());
	         existingTeam.setDriver3(team.getDriver3());
	         existingTeam.setTeam(team.getTeam());
	         existingTeam.setBudget(team.getBudget());
	         
	         Fantasyteam updatedTeam = fantasyJpa.save(existingTeam);
	         return updatedTeam;
		} else {
			 throw new Exception("Team not found with id " + team.getId());
		}
		
	}
	

}
