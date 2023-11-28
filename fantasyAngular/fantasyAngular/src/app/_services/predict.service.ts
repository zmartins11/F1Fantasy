import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Prediction } from '../model/Prediction';

@Injectable({
  providedIn: 'root'
})
export class PredictService {
  
  
  constructor(private http : HttpClient) { }

 
  savePrediction(first:number, second: number, third:number, user:string, round: string): Observable<Prediction> {

    const predictionDTO = {
      first: first.toString(),
      second: second.toString(),
      third: third.toString(),
      user: user,
      round: round
    };

    return this.http.post<Prediction>("http://localhost:8080/predict", predictionDTO);
  }
}
