package com.example.demo.model.fantasy;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;


/**
 * The persistent class for the currentteam database table.
 * 
 */
@Entity
@NamedQuery(name="Currentteam.findAll", query="SELECT c FROM Currentteam c")
public class Currentteam implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int driverId;

	private int fantasyTeamId;

	private int teamId;

	public Currentteam() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDriverId() {
		return this.driverId;
	}

	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}

	public int getFantasyTeamId() {
		return this.fantasyTeamId;
	}

	public void setFantasyTeamId(int fantasyTeamId) {
		this.fantasyTeamId = fantasyTeamId;
	}

	public int getTeamId() {
		return this.teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

}