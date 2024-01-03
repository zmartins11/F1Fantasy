import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Prediction } from '../model/Prediction';

@Injectable({
  providedIn: 'root'
})
export class PredictService {
  
  
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

    return this.http.post<Prediction>("http://localhost:8080/predict", predictionDTO);
  }

  private convertToNullIfZero(value: number): string | null {
    return value === 0 ? null : value.toString();
  }
}
