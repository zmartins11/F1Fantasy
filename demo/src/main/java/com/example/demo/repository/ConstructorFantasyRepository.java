package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.fantasy.Constructorf;

@Repository
public interface ConstructorFantasyRepository extends JpaRepository<Constructorf, Integer> {

}
