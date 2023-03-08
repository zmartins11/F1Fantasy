package com.example.demo.model.fantasy;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;


/**
 * The persistent class for the currentteam database table.
 * 
 */
@Entity
public class FantasyTeam implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Constructorf team;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver1_id")
    private Driverf driver1;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver2_id")
    private Driverf driver2;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver3_id")
    private Driverf driver3;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Constructorf getTeam() {
		return team;
	}

	public void setTeam(Constructorf team) {
		this.team = team;
	}

	public Driverf getDriver1() {
		return driver1;
	}

	public void setDriver1(Driverf driver1) {
		this.driver1 = driver1;
	}

	public Driverf getDriver2() {
		return driver2;
	}

	public void setDriver2(Driverf driver2) {
		this.driver2 = driver2;
	}

	public Driverf getDriver3() {
		return driver3;
	}

	public void setDriver3(Driverf driver3) {
		this.driver3 = driver3;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
	

}