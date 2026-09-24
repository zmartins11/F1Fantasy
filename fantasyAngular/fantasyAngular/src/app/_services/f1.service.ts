import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Driver } from '../model/Driver';
import { Race } from '../model/Race';
import { environment } from '../environments/environment';
import { RaceResults } from '../model/RaceResults';
import { Standings } from '../model/Standings';
import { RaceInfo } from '../model/RaceInfo';

@Injectable({
  providedIn: 'root'
})
export class F1Service {
  
  private baseApi = environment.f1ApiUrl;
  private baseUrl = this.baseApi;

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
    return this.http.get<RaceResults>(`${this.baseApi}/raceResult/${season}/${round}`
    );
  }

  getStandings(): Observable<Standings> {
    return this.http.get<Standings>(`${this.baseApi}/standings`);
  }

  getNextRaceDetails(): Observable<Race> {
    return this.http.get<Race>(`${this.baseApi}/nextRaceDetails`);
  }

  getAllRaces(): Observable<RaceInfo[]> {
    return this.http.get<RaceInfo[]>(`${this.baseApi}/allRaces`);
  }
}
