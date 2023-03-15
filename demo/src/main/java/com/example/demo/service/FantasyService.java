package com.example.demo.service;

import java.rmi.NotBoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.fantasy.Driverf;
import com.example.demo.model.fantasy.FantasyTeam;
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
	
	public Optional<FantasyTeam> getFantasyTeam(int userId) {
		Optional<FantasyTeam> team = fantasyJpa.findByUserId(userId);
		return team;
	}

	public FantasyTeam updateTeam(FantasyTeam team) throws Exception {
		Optional<FantasyTeam> optionalTeam = fantasyJpa.findById(team.getId());
		
		if(optionalTeam.isPresent()) {
			FantasyTeam existingTeam = optionalTeam.get();
			
			 existingTeam.setDriver1(team.getDriver1());
	         existingTeam.setDriver2(team.getDriver2());
	         existingTeam.setDriver3(team.getDriver3());
	         existingTeam.setTeam(team.getTeam());
	         existingTeam.setBudget(team.getBudget());
	         
	         FantasyTeam updatedTeam = fantasyJpa.save(existingTeam);
	         return updatedTeam;
		} else {
			 throw new Exception("Team not found with id " + team.getId());
		}
		
	}
	

}
