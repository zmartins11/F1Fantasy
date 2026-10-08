package com.example.f1.f1_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StandingsDto {

    private List<TotalPointsWC> drivers;
    private List<TotalPointsWC> constructors;
}
