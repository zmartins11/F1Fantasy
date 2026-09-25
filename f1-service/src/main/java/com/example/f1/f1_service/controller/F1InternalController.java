package com.example.f1.f1_service.controller;

import com.example.f1.f1_service.dto.NextRaceData;
import com.example.f1.f1_service.service.ErgastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/f1")
public class F1InternalController {

    private final ErgastService ergastService;

    public F1InternalController(ErgastService ergastService) {
        this.ergastService = ergastService;
    }

    @GetMapping
    public NextRaceData findBySeasonAndRound() throws JsonProcessingException {
        return ergastService.getNextRaceInfo();
    }
}
