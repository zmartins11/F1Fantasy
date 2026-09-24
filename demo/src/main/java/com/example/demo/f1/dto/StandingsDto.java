package com.example.demo.f1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StandingsDto {

    private List<TotalPointsWC> drivers;
    private List<TotalPointsWC> constructors;
}
