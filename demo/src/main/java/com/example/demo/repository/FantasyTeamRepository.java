package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FantasyTeamRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	




	public int saveFantasyTeam(int d1, int d2, int d3, int team, int user) {
//		String sql  = "insert into fantasy_team (driver1_id, driver2_id, driver3_id, team_id, user_id) values(?,?,?,?,?)";
		return jdbcTemplate.update("insert into fantasy_team (driver1_id, driver2_id, driver3_id, team_id, user_id) values(?,?,?,?,?)", d1, d2, d3, team, user);
	}

}
