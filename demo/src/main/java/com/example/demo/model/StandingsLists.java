package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StandingsLists {

    private String season;
    private String round;
    @JsonProperty("DriverStandings")
    private List<DriverStanding> driverStandings;
    @JsonProperty("ConstructorStandings")
    private List<ConstructorStandings> constructorStandings;
}
