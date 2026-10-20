
import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { F1Service } from 'src/app/_services/f1.service';

import { AuthService } from 'src/app/_services/auth.service';
import { ScoringService } from 'src/app/_services/scoring.service';
import { PredictService } from 'src/app/_services/predict.service';



import {ScoringHistory, ScoringHistoryPage, ScoringHistoryView} from 'src/app/model/scoring-history.model';
import { RaceInfo } from 'src/app/model/RaceInfo';

import { Observable, throwError } from 'rxjs';
import { catchError, shareReplay } from 'rxjs/operators';

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

  selectedSeason: number = 2026;

  // SEASON STATS
  playerStats: PlayerStats | null = null;

  loadingStats: boolean = false;
  statsError: boolean = false;

  // PREDICTION HISTORY
  scoringHistory: ScoringHistoryView[] = [];

  historyPage: number = 0;
  historyPageSize: number = 5;
  historyTotalPages: number = 0;
  historyTotalElements: number = 0;

  loadingHistory: boolean = false;
  historyError: boolean = false;

  private racesCache = new Map<number, Observable<RaceInfo[]>>();

  constructor(
    private authService: AuthService,
    private predictionService: PredictService,
    private scoringService: ScoringService,
    private f1Service: F1Service
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getUser();

    if (this.currentUser) {
      this.loadSeasonStats();
      this.loadScoringHistory();
    }
  }

  // MY SEASON STATS
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

  // PREDICTION HISTORY
  loadScoringHistory(page: number = 0): void {

  this.loadingHistory = true;
  this.historyError = false;

  forkJoin({
    history: this.scoringService.getScoringHistory(
      this.selectedSeason,
      page,
      this.historyPageSize
    ),
    races: this.getAllRacesCached(this.selectedSeason)
  })
  .pipe(
    finalize(() => {
      this.loadingHistory = false;
    })
  )
  .subscribe({
    next: ({ history, races }) => {

      const racesMap = new Map<number, RaceInfo>();

      races.forEach(race => {
        racesMap.set(Number(race.round), race);
      });

      this.scoringHistory = history.content.map(result => {

        const raceInfo = racesMap.get(result.round);

        return {
          ...result,
          raceName: raceInfo?.raceName ?? `Round ${result.round}`,
          country: raceInfo?.country ?? ''
        };
      });

      this.historyPage = history.number;
      this.historyTotalPages = history.totalPages;
      this.historyTotalElements = history.totalElements;
    },

    error: (error) => {
      console.error('Error loading scoring history:', error);
      this.scoringHistory = [];
      this.historyError = true;
    }
  });
}

  // NEXT HISTORY PAGE
  nextHistoryPage(): void {
    if (!this.loadingHistory &&
        this.historyPage + 1 < this.historyTotalPages) {

      this.loadScoringHistory(this.historyPage + 1);
    }
  }

  // PREVIOUS HISTORY PAGE
  previousHistoryPage(): void {
    if (!this.loadingHistory && this.historyPage > 0) {

      this.loadScoringHistory(this.historyPage - 1);
    }
  }

  getAllRacesCached(season: number): Observable<RaceInfo[]> {

  const cachedRaces = this.racesCache.get(season);

  if (cachedRaces) {
    return cachedRaces;
  }

  const request$ = this.f1Service.getAllRaces(season).pipe(
    shareReplay({
      bufferSize: 1,
      refCount: false
    }),
    catchError(error => {
      this.racesCache.delete(season);
      return throwError(() => error);
    })
  );

  this.racesCache.set(season, request$);

  return request$;
}
}
