import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { Driver } from "../common/driver";
import { Race } from "../common/race";

@Injectable({
    providedIn: 'root'
})
export class f1Service {

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
