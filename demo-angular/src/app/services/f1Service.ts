import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { Driver } from "../common/driver";
import { Race } from "../common/race";
import { RaceResults } from "../common/race-results";

@Injectable({
    providedIn: 'root'
})
export class f1Service {

    private baseUrl = "http://ec2-16-16-76-107.eu-north-1.compute.amazonaws.com/rawData";

    constructor(private httpClient: HttpClient) { }

    raceData : any;

    setRaceData(data: any) {
        this.raceData = data;
      }
    
    getRaceData() {
        return this.raceData;
      }

    getRaces(season : number): Observable<Race[]> {
        return this.httpClient.get<Race[]>(`http://ec2-16-16-76-107.eu-north-1.compute.amazonaws.com/${season}`);
    }

    getDriversList(season: number): Observable<Driver[]> {
        const search = `${this.baseUrl}/${season}`;
        return this.httpClient.get<Driver[]>(search);
    }

    getRaceResult(season: string, round: string) {
        return this.httpClient.get<RaceResults[]>(`http://ec2-16-16-76-107.eu-north-1.compute.amazonaws.com/raceResult/${season}/${round}`);
    }

}
