package com.example.f1.scoring_service.controller;

import com.example.f1.scoring_service.contracts.race.RaceResultData;
import com.example.f1.scoring_service.dto.PointsInfoDto;
import com.example.f1.scoring_service.dto.ScoringHistoryDto;
import com.example.f1.scoring_service.dto.ScoringStatsDto;
import com.example.f1.scoring_service.dto.TotalPointsDto;
import com.example.f1.scoring_service.service.ScoringService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/me/stats/{season}")
    public ResponseEntity<ScoringStatsDto> getScoringStatsBySeason(@PathVariable Integer season, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(scoringService.getScoringStatsBySeason(jwt.getClaim("userId"), season));
    }

    @GetMapping("/me/stats/history/{season}")
    public ResponseEntity<Page<ScoringHistoryDto>> getScoringStatsBySeason(@PathVariable Integer season,
                                                                           @AuthenticationPrincipal Jwt jwt,
                                                                           @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(scoringService.getScoringHistoryBySeason(Math.toIntExact(jwt.getClaim("userId")), season, pageable));
    }

}
