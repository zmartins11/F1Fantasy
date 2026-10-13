package com.example.f1.scoring_service.dto;

public record ScoringStatsDto(Long totalPoints,
                              Integer leaderboardRank,
                              Long correctPodiums) {
}
