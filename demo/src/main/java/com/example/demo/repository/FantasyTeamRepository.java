package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.model.fantasy.FantasyTeam;

@Repository
public class FantasyTeamRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	

	public List<FantasyTeam> getFantasyTeamsByUserId(int userId) {
	    String query = "SELECT * FROM fantasy_team WHERE user_id = ?";
	    RowMapper<FantasyTeam> rowMapper = new BeanPropertyRowMapper<>(FantasyTeam.class);
	    List<FantasyTeam> teams = jdbcTemplate.query(query, rowMapper, userId);
	    return teams;
	}

	public int saveFantasyTeam(int d1, int d2, int d3, int team, int user) {
		return jdbcTemplate.update("insert into fantasy_team (driver1_id, driver2_id, driver3_id, team_id, user_id) "
				+ "values(?,?,?,?,?)", d1, d2, d3, team, user);
	}

}
