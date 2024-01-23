import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-weather',
  templateUrl: './weather.component.html',
  styleUrls: ['./weather.component.css']
})
export class WeatherComponent {

  weatherIconMapping: { [key: string]: string } = {
    'clear sky': 'fas fa-sun',
    'few clouds': 'fas fa-cloud-sun',
    'scattered clouds': 'fas fa-cloud',
    'broken clouds': 'fas fa-cloud',
    // Add more mappings as needed
  };


  @Input() weather : any;

}
