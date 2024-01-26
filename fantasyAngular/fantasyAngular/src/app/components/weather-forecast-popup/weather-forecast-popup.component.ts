import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { WeatherService } from 'src/app/_services/weather.service';
import { Weather } from 'src/app/model/Weather';

@Component({
  selector: 'app-weather-forecast-popup',
  templateUrl: './weather-forecast-popup.component.html',
  styleUrls: ['./weather-forecast-popup.component.css']
})
export class WeatherForecastPopupComponent implements OnInit {

  constructor(public bsModalRef: BsModalRef, private weatherService: WeatherService)  { }

  weatherData: Weather[] = [];
  country : string ='';
  city : string = "";


  ngOnInit(): void {
    console.log("errorMESSA:");
    console.log('')
    this.weatherService.getWeatherForecast(this.country,this.city,0, 0, 0, true).subscribe((response:any) => {
      const weatherInfo = response.weather;
      this.weatherData = weatherInfo;
    })
  }

  getWeatherIconClass(weather: string): string {
    if (weather.includes('clear sky')) {
      return 'fa-solid fa-sun';
    } else if (weather.includes('cloud')) {
      return 'fa-solid fa-cloud';
    } else if (weather.includes('rain') || weather.includes('shower rain')) {
      return 'fa-solid fa-cloud-rain';
    } else if (weather.includes('thunderstorm')) {
      return 'fa-solid fa-cloud-bolt';
    } else if (weather.includes('snow')) {
      return 'fa-solid fa-cloud-meatball';
    } else if (
      weather.includes('mist') ||
      weather.includes('smoke') ||
      weather.includes('haze') ||
      weather.includes('dust') ||
      weather.includes('fog') ||
      weather.includes('sand') ||
      weather.includes('ash') ||
      weather.includes('squall') ||
      weather.includes('tornado')
    ) {
      return 'fa-solid fa-smog';
    } else {
      return 'fa-solid fa-cloud-sun'; // Default icon for unknown weather
    }
  }

}


