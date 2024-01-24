import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-weather',
  templateUrl: './weather.component.html',
  styleUrls: ['./weather.component.css']
})
export class WeatherComponent {

  

  @Input() weather : any;

  getWeatherIconClass(weather: string): string {
    // Your logic to map weather conditions to FontAwesome icon classes
    switch (weather) {
      case 'clear sky':
        return 'fas fa-sun';
      case 'clouds':
        return 'fas fa-cloud';
      // Add more cases as needed
      default:
        return 'fas fa-question'; // Default icon for unknown weather
    }
  }

}
