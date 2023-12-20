package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class StandingsDto {

    private List<TotalPointsDto> drivers;
    private List<TotalPointsDto> constructors;
}
