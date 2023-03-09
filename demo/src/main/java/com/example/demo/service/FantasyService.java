package com.example.demo.service;

import java.util.List;

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
	
	public FantasyTeam getFantasyTeam(int userId) {
		List<FantasyTeam> team = fantasyJpa.findAll();
		FantasyTeam t = team.get(0);
		return t;
	}
	

}
