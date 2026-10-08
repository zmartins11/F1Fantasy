import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Race } from '../model/Race';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})


export class CalendarService {


  private baseApi = `${environment.apiUrl}/api/f1`;
  
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


}
