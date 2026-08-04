import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Driver } from '../model/Driver';
import { Race } from '../model/Race';
import { environment } from '../environments/environment';
import { RaceResults } from '../model/RaceResults';

@Injectable({
  providedIn: 'root'
})
export class F1Service {
  
  private baseApi = environment.apiSpringUrl;
  private baseUrl = `${this.baseApi}/rawData`;

  constructor(private http: HttpClient) { }

  raceData: any;

  setRaceData(data: any) {
    this.raceData = data;
  }

  getRaceData() {
    return this.raceData;
  }

  getDriversList(season: number): Observable<Driver[]> {
    const search = `${this.baseUrl}/${season}`;
    return this.http.get<Driver[]>(search);
  }


 getRaceResults(season: string, round: string): Observable<RaceResults> {
    return this.http.get<RaceResults>(`${this.baseApi}/raceResult/${season}/${round}`
    );
  }
}
