import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AuthService } from 'src/app/_services/auth.service';
import { ScoringService } from 'src/app/_services/scoring.service';
import { PredictService } from 'src/app/_services/predict.service';

export interface PlayerStats {
  totalPoints: number;
  leaderboardRank: number | null;
  predictionsMade: number;
  correctPodiums: number | null;
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentUser: any;

  playerStats: PlayerStats | null = null;

  selectedSeason: number = 2026;

  loadingStats: boolean = false;
  statsError: boolean = false;

  constructor(
    private authService: AuthService,
    private predictionService: PredictService,
    private scoringService: ScoringService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getUser();

    if (this.currentUser) {
      this.loadSeasonStats();
    }
  }

  loadSeasonStats(): void {
    this.loadingStats = true;
    this.statsError = false;

    forkJoin({
      prediction: this.predictionService.getPredictionStats(
        this.selectedSeason
      ),
      scoring: this.scoringService.getScoringStats(
        this.selectedSeason
      )
    })
    .pipe(
      finalize(() => {
        this.loadingStats = false;
      })
    )
    .subscribe({
      next: ({ prediction, scoring }) => {

        this.playerStats = {
          totalPoints: scoring.totalPoints,
          leaderboardRank: scoring.leaderboardRank,
          predictionsMade: prediction.predictionsMade,
          correctPodiums: scoring.correctPodiums
        };

      },
      error: (error) => {
        console.error('Error loading player stats:', error);
        this.playerStats = null;
        this.statsError = true;
      }
    });
  }
}