import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { DateTimeResponse } from '../model/DateTimeResponse';
import { NextRaceInfo } from '../model/NextRaceInfo';
import { TotalPointsResponse } from '../model/TotalPointsResponse';
import { PointsInfo } from '../model/PointsInfo';
import { Standings } from '../model/Standings';
import { RaceInfo } from '../model/RaceInfo';
import { Race } from '../model/Race';
import { data } from 'jquery';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DateTimeServiceService {


  private apiUrl = environment.apiSpringUrl;
  loader = new BehaviorSubject<Boolean>(false);

  constructor(private http: HttpClient) { }

  getNextRaceInfo(userId: number): Observable<NextRaceInfo> {
    return this.http.get<NextRaceInfo>(`${this.apiUrl}/raceSchedule`, {
      params: new HttpParams().set('userId', userId)
    });
  }

  getTotalPoints(username: string): Observable<TotalPointsResponse> {
    return this.http.get<TotalPointsResponse>(`${this.apiUrl}/totalPoints`, {
      params: new HttpParams().set('username', username)
    });
  }

  getPointsInfo(userId: number): Observable<PointsInfo []> {
    return this.http.get<PointsInfo[]>(`${this.apiUrl}/pointsInfo`, {
      params: new HttpParams().set('userId', userId)
    });
  }

  getStandingsSeason(): Observable<Standings> {
    const url = `${this.apiUrl}/standings`;
    return this.http.get<Standings>(url);
  }

  getNextRaceDetails(): Observable<Race> {
    const url = `${this.apiUrl}/nextRaceDetails`;
    return this.http.get<Race>(url);
  }

  getAllRaces(): Observable<RaceInfo []> {
    const url = `${this.apiUrl}/allRaces`;
    return this.http.get<RaceInfo[]>(url);
  }


  
}
