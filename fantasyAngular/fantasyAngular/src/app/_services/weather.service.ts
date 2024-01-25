import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Weather } from '../model/Weather';

@Injectable({
  providedIn: 'root'
})
export class WeatherService {

  constructor(private http : HttpClient) { }

  getWeather(country:string, city: string, hour: number, day: number, month:number): Observable<Weather> {
    console.log('TESTE SEEFIVCECEPOR:' + hour + day )
    const apiPython = " http://127.0.0.1:5000/weather"
    const params = {
      country: country,
      city: city,
      hour: hour.toString(),
      day: day.toString(),
      month: month.toString()
    };
    const headers = new HttpHeaders();

    return this.http.get<Weather>(apiPython, {params, headers});
  }
}
