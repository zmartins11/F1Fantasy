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


   races : Race[] = [];
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


 
  getRaceResults(season: string, round :string) {
   
  }



  openRaceDetails(race: Race) {
    this.f1Service.setRaceData(race);

    const initialState = {
      title: race.raceName,
      race: race
    };
    this.modalService.show(RaceDetailsComponent, { initialState });
    
  }

  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

}

