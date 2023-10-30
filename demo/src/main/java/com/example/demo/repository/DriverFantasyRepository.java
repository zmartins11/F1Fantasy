package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.fantasy.Driverf;

@Repository
public interface DriverFantasyRepository extends JpaRepository<Driverf, Integer> {

}
