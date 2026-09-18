package com.example.demo.f1.model;

import com.example.demo.f1.model.Constructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

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
