package com.example.demo.repository;

import com.example.demo.model.fantasy.DriversPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriversPointsRepository extends JpaRepository<DriversPoints, Integer> {
    public DriversPoints findByDriver(String driver);
}
