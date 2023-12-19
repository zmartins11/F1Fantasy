package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class StandingsDto {

    private Map<Integer, Map<String, String>> driverStandings;
    private Map<Integer, Map<String, String>> constructorStandings;
}
