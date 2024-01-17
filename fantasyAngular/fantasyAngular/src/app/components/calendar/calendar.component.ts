import { Component } from '@angular/core';
import { Race } from 'src/app/model/Race';
import { RaceDetailsComponent } from '../race-details/race-details.component';
import { BsModalService } from 'ngx-bootstrap/modal';
import { CalendarService } from 'src/app/_services/calendar.service';
import { F1Service } from 'src/app/_services/f1.service';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent {


  races : Race[] = [];
  currentSeason! : number;

  currentYear = new Date().getFullYear();

  constructor(private calendarService: CalendarService,
              private f1Service: F1Service,
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
    this.calendarService.getRaces(2023).subscribe(data => {
      this.races = data;
      console.log(this.races);
    })
  }


 
  getRaceResults(season: string, round :string) {
   
  }



  openRaceDetails(race: Race) {
    this.f1Service.setRaceData(race);

    const initialState = {
      race: race
    };

    if(this.currentSeason === this.currentYear) {
      this.modalService.show(RaceDetailsComponent, { initialState });
    } else {
      this.modalService.show(RaceDetailsComponent, { initialState })
    }
    
    
  }

  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

}
