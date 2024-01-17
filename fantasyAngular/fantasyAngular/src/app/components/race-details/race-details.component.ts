import { Component, OnInit } from '@angular/core';
import { F1Service } from 'src/app/_services/f1.service';
import { Race } from 'src/app/model/Race';
import { RaceResults } from 'src/app/model/RaceResults';

@Component({
  selector: 'app-race-details',
  templateUrl: './race-details.component.html',
  styleUrls: ['./race-details.component.css']
})
export class RaceDetailsComponent implements OnInit {

  raceData! : Race;

  raceResults! : RaceResults;

  race!: Race;

  constructor(private f1Service: F1Service) { }

  ngOnInit(): void {
    this.raceData = this.f1Service.getRaceData();
  }

}
