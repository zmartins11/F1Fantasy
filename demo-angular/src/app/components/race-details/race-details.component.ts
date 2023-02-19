import { Component, OnInit } from '@angular/core';
import { f1Service } from 'src/app/services/f1Service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Race } from 'src/app/common/race';
import { RaceResults } from 'src/app/common/race-results';



@Component({
  selector: 'app-race-details',
  templateUrl: './race-details.component.html',
  styleUrls: ['./race-details.component.css']
})
export class RaceDetailsComponent implements OnInit {


  raceData! : Race;

  raceResults! : RaceResults;

  race!: Race;

  constructor(private f1Service: f1Service) { }

  ngOnInit(): void {
    this.raceData = this.f1Service.getRaceData();


  }


}
