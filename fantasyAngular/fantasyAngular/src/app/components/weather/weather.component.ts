import { Component, Input } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { WeatherForecastPopupComponent } from '../weather-forecast-popup/weather-forecast-popup.component';

@Component({
  selector: 'app-weather',
  templateUrl: './weather.component.html',
  styleUrls: ['./weather.component.css']
})
export class WeatherComponent {


  constructor(private modalService: BsModalService){}

  bsModalRef: BsModalRef | undefined;

  @Input() weather : any;
  @Input() city : string = '';
  @Input() country : string = '';

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


  onWeatherWidgetClick() {
    // Fetch races from your service and subscribe to the observable

    const initialState = {
      country: this.country,
      city: this.city
    };

    const modalOptions = {
      initialState,
      class: 'modal-lg',
    };

      this.bsModalRef = this.modalService.show(WeatherForecastPopupComponent, modalOptions );
   
  }


  onWeatherWidgetMouseOver() {
    const weatherWidget = document.getElementById('weather-widget');
    if (weatherWidget) {
      weatherWidget.style.backgroundColor = '#e6f7ff';
    }
  }
  
  onWeatherWidgetMouseOut() {
    const weatherWidget = document.getElementById('weather-widget');
    if (weatherWidget) {
      weatherWidget.style.backgroundColor = '';
    }
  }
  
  
  

}
