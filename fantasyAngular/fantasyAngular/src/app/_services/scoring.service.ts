import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TotalPointsUsers } from '../model/TotalPointsUsers';
import { PointsInfo } from '../model/PointsInfo';
import { environment } from 'src/environments/environment';
import { ScoringStats } from '../model/player-stats.model';
import { ScoringHistoryPage } from '../model/scoring-history.model';

@Injectable({
  providedIn: 'root'
})
export class ScoringService {
  private readonly baseApi = `${environment.apiUrl}/api/scoring`;

  constructor(private http: HttpClient) { }

  getTotalPoints(): Observable<TotalPointsUsers[]> {
    return this.http.get<TotalPointsUsers[]>(`${this.baseApi}/totalPoints`);
  }

  getPointsInfo(): Observable<PointsInfo[]> {
    return this.http.get<PointsInfo[]>(`${this.baseApi}/pointsInfo`);
  }

  getScoringStats(season: number): Observable<ScoringStats> {
    return this.http.get<ScoringStats>(
      `${this.baseApi}/me/stats/${season}`
    );
  }

  getScoringHistory(
  season: number,
  page: number = 0,
  size: number = 5): Observable<ScoringHistoryPage> {

  return this.http.get<ScoringHistoryPage>(
    `${this.baseApi}/me/stats/history/${season}`,
    {
      params: {
        page: page.toString(),
        size: size.toString()
      }
    }
  );
}



}
