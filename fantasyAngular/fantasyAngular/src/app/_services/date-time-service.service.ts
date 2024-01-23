import { HttpClient, HttpHeaders } from '@angular/common/http';
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

@Injectable({
  providedIn: 'root'
})
export class DateTimeServiceService {


  private apiUrl = 'http://localhost:8080';
  private apiKeyWeather = '6d61bbd868dc7299fdc44c9543ad3988';
  private apiUrlWeather = '/api/data/2.5/forecast';
  loader = new BehaviorSubject<Boolean>(false);

  constructor(private http: HttpClient) { }

  getNextRaceInfo(username: string): Observable<NextRaceInfo> {
    const url = `${this.apiUrl}/raceSchedule?username=${username}`;
    return this.http.get<NextRaceInfo>(url);
  }

  getTotalPoints(username: string): Observable<TotalPointsResponse> {
    const url = `${this.apiUrl}/totalPoints?username=${username}`;
    return this.http.get<TotalPointsResponse>(url);
  }

  getPointsInfo(username: string): Observable<PointsInfo []> {
    const url = `${this.apiUrl}/pointsInfo?username=${username}`;
    return this.http.get<PointsInfo[]>(url);
  }

  getStandingsSeason(): Observable<Standings> {
    const url = `${this.apiUrl}/standings`;
    return this.http.get<Standings>(url);
  }

  getAllRaces(): Observable<RaceInfo []> {
    const url = `${this.apiUrl}/allRaces`;
    return this.http.get<RaceInfo[]>(url);
  }


  
}
