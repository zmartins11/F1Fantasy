package com.example.demo.repository;

import com.example.demo.model.fantasy.RaceResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaceResultRepository extends JpaRepository<RaceResult, String> {
}
