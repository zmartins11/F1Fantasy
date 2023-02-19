import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Race } from 'src/app/common/race';
import { f1Service } from 'src/app/services/f1Service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { RaceDetailsComponent } from '../race-details/race-details.component';
import { RaceResults } from 'src/app/common/race-results';
import { RaceResultsComponent } from '../race-results/race-results.component';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {


  races : Race[] = [];
  raceResults : RaceResults[] = [];
  currentSeason! : number;

  currentYear = new Date().getFullYear();

  constructor(private f1Service: f1Service,
              private modalService: BsModalService) { }

  

  ngOnInit(): void {
    this.currentSeason = new Date().getFullYear();
    this.getRaces();
  }

  showPrevSeason() {
    this.currentSeason -= 1;
    this.getRaces();
  };

  showNextSeason() {
    this.currentSeason += 1;
    console.log(this.currentSeason);
  }

  
  getRaces() {
    this.f1Service.getRaces(this.currentSeason).subscribe(data => {
      this.races = data;
    })
  }






  openRaceDetails(race: Race) {
    this.f1Service.setRaceData(race);

    const initialState = {
      race: race
    };

    if(this.currentSeason === this.currentYear) {
      this.modalService.show(RaceDetailsComponent, { initialState });
    } else {
      this.modalService.show(RaceResultsComponent, { initialState })
    }
    
    
  }

  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

}

