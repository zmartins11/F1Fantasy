import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-weather-forecast-popup',
  templateUrl: './weather-forecast-popup.component.html',
  styleUrls: ['./weather-forecast-popup.component.css']
})
export class WeatherForecastPopupComponent implements OnInit {

  constructor(public bsModalRef: BsModalRef)  { }


  ngOnInit(): void {
    console.log("errorMESSA:");
  }

}
