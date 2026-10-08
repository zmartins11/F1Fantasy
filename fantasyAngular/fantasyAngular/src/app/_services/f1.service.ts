import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Driver } from '../model/Driver';
import { Race } from '../model/Race';
import { RaceResults } from '../model/RaceResults';
import { Standings } from '../model/Standings';
import { RaceInfo } from '../model/RaceInfo';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class F1Service {
  
  private baseApi = `${environment.apiUrl}`;
  private baseUrl = `${this.baseApi}/api/f1`;

  constructor(private http: HttpClient) { }

  raceData: any;

  setRaceData(data: any) {
    this.raceData = data;
  }

  getRaceData() {
    return this.raceData;
  }

  getDriversList(season: number): Observable<Driver[]> {
    const search = `${this.baseUrl}/drivers/${season}`;
    return this.http.get<Driver[]>(search);
  }


 getRaceResults(season: string, round: string): Observable<RaceResults> {
    return this.http.get<RaceResults>(`${this.baseUrl}/raceResult/${season}/${round}`
    );
  }

  getStandings(): Observable<Standings> {
    return this.http.get<Standings>(`${this.baseUrl}/standings`);
  }

  getNextRaceDetails(): Observable<Race> {
    return this.http.get<Race>(`${this.baseUrl}/nextRaceDetails`);
  }

  getAllRaces(): Observable<RaceInfo[]> {
    return this.http.get<RaceInfo[]>(`${this.baseUrl}/allRaces`);
  }
}
