import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Race } from 'src/app/common/race';
import { f1Service } from 'src/app/services/f1Service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { RaceDetailsComponent } from '../race-details/race-details.component';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {

  constructor(private f1Service: f1Service,
              private modalService: BsModalService) { }
  races : Race[] = [];

  ngOnInit(): void {
    this.getRaces();
  }

  
  getRaces() {
    this.f1Service.getRaces().subscribe(data => {
      this.races = data;
    })
  }

  openRaceDetails(race: Race) {
    this.f1Service.setRaceData(race);

    const initialState = {
      title: race.raceName,
      race: race
    };
    this.modalService.show(RaceDetailsComponent, { initialState });
    
  }

}

