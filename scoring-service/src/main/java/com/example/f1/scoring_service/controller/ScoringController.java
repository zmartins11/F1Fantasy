package com.example.f1.scoring_service.controller;

import com.example.f1.scoring_service.contracts.race.RaceResultData;
import com.example.f1.scoring_service.dto.PointsInfoDto;
import com.example.f1.scoring_service.dto.TotalPointsDto;
import com.example.f1.scoring_service.service.ScoringService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scoring")
public class ScoringController {

    private final ScoringService scoringService;

    public ScoringController(ScoringService scoringService) {
        this.scoringService = scoringService;
    }

    @GetMapping("/pointsInfo")
    public ResponseEntity<List<PointsInfoDto>> getPointsInfo(@AuthenticationPrincipal Jwt jwt) throws JsonProcessingException {
        RaceResultData latestRaceFinished = scoringService.latestRaceFinished();
        if (latestRaceFinished != null) {
            return ResponseEntity.ok(scoringService.getPointsInfo(jwt.getClaim("userId"), latestRaceFinished));
        } else {
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/totalPoints")
    public ResponseEntity<List<TotalPointsDto>> getTotalPoints() {
        return ResponseEntity.ok(scoringService.getTotalPoints());
    }
}
