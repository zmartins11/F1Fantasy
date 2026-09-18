package com.example.demo.scoring.controller;

import com.example.demo.scoring.dto.PointsInfoDto;
import com.example.demo.scoring.dto.TotalPointsDto;
import com.example.demo.f1.model.RaceResult;
import com.example.demo.scoring.port.RaceResultData;
import com.example.demo.scoring.service.ScoringService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ScoringController {

    private final ScoringService scoringService;

    public ScoringController(ScoringService scoringService) {
        this.scoringService = scoringService;
    }

    @GetMapping("/pointsInfo")
    public ResponseEntity<List<PointsInfoDto>> getPointsInfo(Authentication authentication) throws JsonProcessingException {
        RaceResultData racedPassed = scoringService.getRacePassed();
        if (racedPassed != null) {
            Integer userId = scoringService.getAuthenticatedUserId(authentication);
            return ResponseEntity.ok(scoringService.getPointsInfo(userId, String.valueOf(racedPassed.round())));
        } else {
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/totalPoints")
    public ResponseEntity<List<TotalPointsDto>> getTotalPoints() {
        return ResponseEntity.ok(scoringService.getTotalPoints());
    }
}
