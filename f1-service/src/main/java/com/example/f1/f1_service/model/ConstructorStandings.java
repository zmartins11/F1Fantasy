package com.example.f1.f1_service.model;

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
