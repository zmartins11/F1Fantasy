package com.example.demo.repository;

import com.example.demo.model.fantasy.DriversPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriversPointsRepository extends JpaRepository<DriversPoints, Integer> {
    public DriversPoints findByDriver(String driver);
    public List<DriversPoints> findByRaceId(String raceId);
    @Query("SELECT dp FROM DriversPoints dp WHERE dp.driver IN :drivers AND dp.raceId = :raceId")
    List<DriversPoints> findByDriverInAndRaceId(@Param("drivers") List<String> drivers, @Param("raceId") String raceId);

    public List<DriversPoints> findByRaceIdAndDriver(String raceId, String driverId);

}
