package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConstructorStandings {
    private String position;
    private String positionText;
    private String points;
    private String wins;
    @JsonProperty("Constructor")
    private Constructor constructor;
}
