import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Prediction } from '../model/Prediction';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PredictService {

  private baseApi = environment.apiSpringUrl;
  private baseUrl = `${this.baseApi}/predict`;
  
  
  constructor(private http : HttpClient) { }

 
  savePrediction(first:number, second: number, third:number, fastest:number, user:string, round: string): Observable<Prediction> {

    // Helper function to convert 0 to null
  const convertToNullIfZero = (value: number): string | null => {
    return value === 0 ? null : value.toString();
  };

    const predictionDTO = {
      first: convertToNullIfZero(first),
      second: convertToNullIfZero(second),
      third: convertToNullIfZero(third),
      fastestLap: convertToNullIfZero(fastest),
      user: user,
      round: round
    };

    return this.http.post<Prediction>(this.baseUrl, predictionDTO);
  }

  private convertToNullIfZero(value: number): string | null {
    return value === 0 ? null : value.toString();
  }
}
