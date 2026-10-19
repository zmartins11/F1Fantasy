export interface PredictionStats {
  predictionsMade: number;
}

export interface ScoringStats {
  totalPoints: number;
  leaderboardRank: number | null;
  correctPodiums: number | null;
}

export interface PlayerStats {
  totalPoints: number;
  leaderboardRank: number | null;
  predictionsMade: number;
  correctPodiums: number | null;
}