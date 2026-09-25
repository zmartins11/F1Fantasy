import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Prediction } from '../model/Prediction';
import { NextRaceInfo } from '../model/NextRaceInfo';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PredictService {

  private baseApi = environment.apiGatewayUrl;
  private baseUrl = `${this.baseApi}/api/predictions`;
  
  
  constructor(private http : HttpClient) { }

 
  savePrediction(first:number, second: number, third:number, fastest:number, userId: number, round: number, season: number):
   Observable<Prediction> {

    // Helper function to convert 0 to null
  const convertToNullIfZero = (value: number): string | null => {
    return value === 0 ? null : value.toString();
  };

    const predictionDTO = {
      first: convertToNullIfZero(first),
      second: convertToNullIfZero(second),
      third: convertToNullIfZero(third),
      fastestLap: convertToNullIfZero(fastest),
      userId: userId,
      round: round,
      season: season
    };

    return this.http.post<Prediction>(`${this.baseUrl}/predict`, predictionDTO);
  }

  getNextRaceInfo(): Observable<NextRaceInfo> {
    return this.http.get<NextRaceInfo>(`${this.baseUrl}/raceSchedule`);
  }

}
