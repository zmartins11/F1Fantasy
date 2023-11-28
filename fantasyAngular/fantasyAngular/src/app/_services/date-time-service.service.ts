import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { DateTimeResponse } from '../model/DateTimeResponse';
import { NextRaceInfo } from '../model/NextRaceInfo';
import { TotalPointsResponse } from '../model/TotalPointsResponse';

@Injectable({
  providedIn: 'root'
})
export class DateTimeServiceService {

  private apiUrl = 'http://localhost:8080';
  loader = new BehaviorSubject<Boolean>(false);

  constructor(private http: HttpClient) { }

  getNextRaceInfo(username: string): Observable<NextRaceInfo> {
    const url = `${this.apiUrl}/raceSchedule?username=${username}`;
    return this.http.get<NextRaceInfo>(url);
  }

  getTotalPoints(): Observable<TotalPointsResponse> {
    const url = "http://localhost:8080/totalPoints";
    return this.http.get<TotalPointsResponse>(url);
  }
}
