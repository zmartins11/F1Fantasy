import { Component, HostListener, OnInit } from '@angular/core';
import {
  Observable,
  forkJoin,
  throwError
} from 'rxjs';
import {
  catchError,
  finalize,
  shareReplay
} from 'rxjs/operators';

import { AuthService } from 'src/app/_services/auth.service';
import { ScoringService } from 'src/app/_services/scoring.service';
import { PredictService } from 'src/app/_services/predict.service';
import { F1Service } from 'src/app/_services/f1.service';

import {
  ScoringHistoryView
} from 'src/app/model/scoring-history.model';

import { RaceInfo } from 'src/app/model/RaceInfo';
import { Driver } from 'src/app/model/Driver';

export interface PlayerStats {
  totalPoints: number;
  leaderboardRank: number | null;
  predictionsMade: number;
  correctPodiums: number | null;
}

export interface RaceDriverDetail {
  position: string;
  driver: string;
  hit: boolean;
}

export interface RaceDetails {
  actual: RaceDriverDetail[];
  predicted: RaceDriverDetail[];

  fastestLap: {
    actual: string;
    predicted: string;
    hit: boolean;
  };
}

// Shape esperado do endpoint individual do predict-service
interface PredictionDetail {
  id: number;
  first: string | number | null;
  second: string | number | null;
  third: string | number | null;
  fastestLap: string | number | null;
}

// Shape que a transformação do resultado real deve produzir
interface ActualRaceDrivers {
  first: string | number | null;
  second: string | number | null;
  third: string | number | null;
  fastestLap: string | number | null;
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  // Pontos por cada piloto acertado (visual apenas)
  readonly pointsPerHit = 5;

  currentUser: any;
  selectedSeason = 2026;

  // SEASON STATS
  playerStats: PlayerStats | null = null;
  loadingStats = false;
  statsError = false;

  // PREDICTION HISTORY
  scoringHistory: ScoringHistoryView[] = [];

  historyPage = 0;
  historyPageSize = 5;
  historyTotalPages = 0;
  historyTotalElements = 0;

  loadingHistory = false;
  historyError = false;

  // CACHES
  private racesCache =
    new Map<number, Observable<RaceInfo[]>>();

  private driversCache =
    new Map<number, Observable<Driver[]>>();

  // RACE DETAILS MODAL
  selectedRace: ScoringHistoryView | null = null;
  raceDetails: RaceDetails | null = null;

  loadingRaceDetails = false;
  raceDetailsError = false;

  // Impede que uma resposta antiga substitua
  // os detalhes de outra corrida selecionada.
  private detailsRequestId = 0;

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

  // ===========================
  // RACE DETAILS
  // ===========================

  openRaceDetails(history: ScoringHistoryView): void {

    const season = this.selectedSeason;
    const round = history.round;
    const requestId = ++this.detailsRequestId;

    this.selectedRace = history;
    this.raceDetails = null;
    this.loadingRaceDetails = true;
    this.raceDetailsError = false;

    forkJoin({

      prediction: this.predictionService.getPredictionByRace(
        season,
        round
      ),

      actual: this.f1Service.getRaceResults(
        String(season),
        String(round)
      ),

      drivers: this.getDriversCached(season)

    })
    .pipe(
      finalize(() => {
        if (requestId === this.detailsRequestId) {
          this.loadingRaceDetails = false;
        }
      })
    )
    .subscribe({

      next: ({ prediction, actual, drivers }) => {

        if (requestId !== this.detailsRequestId) {
          return;
        }

        // ADAPTAR se RaceResults tiver uma estrutura diferente
        const actualDrivers = this.extractActualDrivers(actual);

        this.raceDetails = this.buildRaceDetails(
          prediction,
          actualDrivers,
          drivers
        );
      },

      error: error => {

        if (requestId !== this.detailsRequestId) {
          return;
        }

        console.error(
          'Error loading race details:',
          error
        );

        this.raceDetailsError = true;
        this.raceDetails = null;
      }
    });
  }

  private buildRaceDetails(
    prediction: PredictionDetail,
    actual: ActualRaceDrivers,
    drivers: Driver[]
  ): RaceDetails {

    // A previsão guarda números de piloto; o /raceResult devolve apelidos.
    // Converte-se tudo para apelido antes de comparar.
    const predictedNames = [
      prediction.first,
      prediction.second,
      prediction.third
    ].map(d => this.getDriverName(d, drivers));

    const actualNames = [
      actual.first,
      actual.second,
      actual.third
    ].map(d => d == null ? 'Not selected' : String(d));

    const predicted = predictedNames.map(
      (driver, index): RaceDriverDetail => ({
        position: String(index + 1),
        driver,
        hit: this.isSameDriver(driver, actualNames[index])
      })
    );

    const actualPodium = actualNames.map(
      (driver, index): RaceDriverDetail => ({
        position: String(index + 1),
        driver,
        hit: this.isSameDriver(driver, predictedNames[index])
      })
    );

    const predictedFastest = this.getDriverName(prediction.fastestLap, drivers);
    const actualFastest = actual.fastestLap == null
      ? 'Not selected'
      : String(actual.fastestLap);

    return {

      predicted,

      actual: actualPodium,

      fastestLap: {
        predicted: predictedFastest,
        actual: actualFastest,
        hit: this.isSameDriver(predictedFastest, actualFastest)
      }
    };
  }

  private isSameDriver(
    predicted: string | number | null,
    actual: string | number | null
  ): boolean {

    if (predicted == null || actual == null || predicted === 'Not selected') {
      return false;
    }

    return String(predicted) === String(actual);
  }

  private getDriverName(
    driverNumber: string | number | null | undefined,
    drivers: Driver[]
  ): string {

    if (driverNumber == null || driverNumber === '') {
      return 'Not selected';
    }

    const driver = drivers.find(
      d => String(d.permanentNumber) === String(driverNumber)
    );

    return driver?.familyName ?? `#${driverNumber}`;
  }

  private extractActualDrivers(
    result: unknown
  ): ActualRaceDrivers {

    // ADAPTAR à interface RaceResults real.
    // Esta versão pressupõe campos first, second,
    // third e fastestLap com números dos pilotos.

    const race = result as Partial<ActualRaceDrivers>;

    if (
      race == null ||
      race.first == null ||
      race.second == null ||
      race.third == null
    ) {
      throw new Error(
        'Unexpected RaceResults response structure'
      );
    }

    return {
      first: race.first,
      second: race.second,
      third: race.third,
      fastestLap: race.fastestLap ?? null
    };
  }

  closeRaceDetails(): void {
    ++this.detailsRequestId;

    this.selectedRace = null;
    this.raceDetails = null;
    this.loadingRaceDetails = false;
    this.raceDetailsError = false;
  }

  @HostListener('document:keydown.escape')
  onEscape(): void {
    if (this.selectedRace) {
      this.closeRaceDetails();
    }
  }

  // ===========================
  // SEASON STATS
  // ===========================

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

      error: error => {
        console.error(
          'Error loading player stats:',
          error
        );

        this.playerStats = null;
        this.statsError = true;
      }
    });
  }

  // ===========================
  // PREDICTION HISTORY
  // ===========================

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
            raceName: raceInfo?.raceName
              ?? `Round ${result.round}`,
            country: raceInfo?.country ?? ''
          };
        });

        this.historyPage = history.number;
        this.historyTotalPages = history.totalPages;
        this.historyTotalElements = history.totalElements;
      },

      error: error => {
        console.error(
          'Error loading scoring history:',
          error
        );

        this.scoringHistory = [];
        this.historyError = true;
      }
    });
  }

  nextHistoryPage(): void {
    if (
      !this.loadingHistory &&
      this.historyPage + 1 < this.historyTotalPages
    ) {
      this.loadScoringHistory(this.historyPage + 1);
    }
  }

  previousHistoryPage(): void {
    if (
      !this.loadingHistory &&
      this.historyPage > 0
    ) {
      this.loadScoringHistory(this.historyPage - 1);
    }
  }

  // ===========================
  // CACHES
  // ===========================

  getAllRacesCached(
    season: number
  ): Observable<RaceInfo[]> {

    const cached = this.racesCache.get(season);

    if (cached) {
      return cached;
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

  getDriversCached(
    season: number
  ): Observable<Driver[]> {

    const cached = this.driversCache.get(season);

    if (cached) {
      return cached;
    }

    const request$ = this.f1Service.getDriversList(season).pipe(

      shareReplay({
        bufferSize: 1,
        refCount: false
      }),

      catchError(error => {
        this.driversCache.delete(season);
        return throwError(() => error);
      })
    );

    this.driversCache.set(season, request$);

    return request$;
  }
}
