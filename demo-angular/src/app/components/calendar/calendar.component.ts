import { Component, OnInit } from '@angular/core';
import { Race } from 'src/app/common/race';
import { f1Service } from 'src/app/services/f1Service';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {

  constructor(private f1Service: f1Service) { }
  races : Race[] = [];

  ngOnInit(): void {
    this.getRaces();
  }

  
  getRaces() {
    this.f1Service.getRaces().subscribe(data => {
      this.races = data;
    })
  }

}

