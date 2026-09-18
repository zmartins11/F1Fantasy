package com.example.demo.f1.dto;

import com.example.demo.scoring.dto.TotalPointsDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StandingsDto {

    private List<TotalPointsDto> drivers;
    private List<TotalPointsDto> constructors;
}
