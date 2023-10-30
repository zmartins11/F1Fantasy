package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.fantasy.Fantasyteam;

@Repository
public interface FantasyTeamJpaRepository extends JpaRepository<Fantasyteam, Integer> {

	Optional<Fantasyteam> findByUserId(int userId);

}
