import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TotalPointsUsers } from '../model/TotalPointsUsers';
import { PointsInfo } from '../model/PointsInfo';
import { environment } from 'src/environments/environment';

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
}
