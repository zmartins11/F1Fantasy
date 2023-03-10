package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.fantasy.FantasyTeam;

@Repository
public interface FantasyTeamJpaRepository extends JpaRepository<FantasyTeam, Integer> {

	Optional<FantasyTeam> findByUserId(int userId);

}
