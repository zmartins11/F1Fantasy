import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Race } from '../model/Race';
import { Driver } from '../model/Driver';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})


export class CalendarService {


  private baseApi = environment.apiSpringUrl;
  private baseUrl = `${this.baseApi}/rawData`;
  

    constructor(private httpClient: HttpClient) { }

    raceData : any;

    setRaceData(data: any) {
        this.raceData = data;
      }
    
    getRaceData() {
        return this.raceData;
      }

    getRaces(season : number): Observable<Race[]> {
        return this.httpClient.get<Race[]>(`${this.baseApi}/${season}`);
    }

    getDriversList(season: number): Observable<Driver[]> {
        const search = `${this.baseUrl}/${season}`;
        return this.httpClient.get<Driver[]>(search);
    }

    getRaceResult() {
        
    }

}
