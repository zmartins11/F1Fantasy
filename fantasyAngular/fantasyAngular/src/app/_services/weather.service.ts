import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Weather } from '../model/Weather';

@Injectable({
  providedIn: 'root'
})
export class WeatherService {

  constructor(private http : HttpClient) { }

  getWeather(query: string): Observable<Weather> {
    const apiPython = " http://127.0.0.1:5000/weather"
    return this.http.get<Weather>(apiPython);
  }
}
