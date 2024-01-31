import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Weather } from '../model/Weather';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WeatherService {

  constructor(private http : HttpClient) { }
  private baseApi = environment.apiPythonUrl;
  private apiPython = `${this.baseApi}/weather`;

  getWeather(country:string, city: string, hour: number, day: number, month:number, forecast : boolean): Observable<Weather> {
    console.log('TESTE SEEFIVCECEPOR:' + hour + day )
    const params = {
      country: country,
      city: city,
      hour: hour.toString(),
      day: day.toString(),
      month: month.toString(),
      forecast : forecast
    };
    const headers = new HttpHeaders();

    return this.http.get<Weather>(this.apiPython, {params, headers});
  }

  getWeatherForecast(country: string, city: string, hour: number, day: number, month: number, forecast: boolean): Observable<Weather[]> {
    
    const params = {
      country: country,
      city: city,
      hour: hour.toString(),
      day: day.toString(),
      month: month.toString(),
      forecast: forecast.toString() // convert boolean to string
    };

    return this.http.get<Weather[]>(this.apiPython, { params });
  }
}
