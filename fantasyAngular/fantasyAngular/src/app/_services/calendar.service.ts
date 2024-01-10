import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Race } from '../model/Race';
import { Driver } from '../model/Driver';

@Injectable({
  providedIn: 'root'
})
export class CalendarService {

  private baseUrl = "http://localhost:8080/rawData";

    constructor(private httpClient: HttpClient) { }

    raceData : any;

    setRaceData(data: any) {
        this.raceData = data;
      }
    
    getRaceData() {
        return this.raceData;
      }

    getRaces(season : number): Observable<Race[]> {
        return this.httpClient.get<Race[]>(`http://localhost:8080/${season}`);
    }

    getDriversList(season: number): Observable<Driver[]> {
        const search = `${this.baseUrl}/${season}`;
        return this.httpClient.get<Driver[]>(search);
    }

    getRaceResult() {
        
    }

}
