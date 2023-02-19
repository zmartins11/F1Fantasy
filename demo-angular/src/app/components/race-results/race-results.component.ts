import { Component, OnInit } from '@angular/core';
import { Race } from 'src/app/common/race';
import { RaceResults } from 'src/app/common/race-results';
import { f1Service } from 'src/app/services/f1Service';

@Component({
  selector: 'app-race-results',
  templateUrl: './race-results.component.html',
  styleUrls: ['./race-results.component.css']
})
export class RaceResultsComponent implements OnInit {


  race!: Race;

  raceResults! : RaceResults;


  constructor(private f1Service: f1Service) { }

  ngOnInit(): void {
    this.f1Service.getRaceResult(this.race.season, this.race.round).subscribe(data => {
      this.raceResults = data[0];
      if (data && data.length > 0 && data[0].Driver) {
        console.log(data[0].Driver.driverId);
      }
     });
     
  }

}
