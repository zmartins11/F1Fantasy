import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { F1Service } from 'src/app/_services/f1.service';
import { Race } from 'src/app/model/Race';
import { RaceResults } from 'src/app/model/RaceResults';

@Component({
  selector: 'app-race-details',
  templateUrl: './race-details.component.html',
  styleUrls: ['./race-details.component.css']
})
export class RaceDetailsComponent implements OnInit {

  race!: Race;
  raceData! : Race;

  raceResult: RaceResults | undefined;

  constructor(public bsModalRef: BsModalRef, private f1Service: F1Service) { }

  getFlagPath(country: string | undefined): string {
    if (!country) {
      return 'assets/images/flags/USA.png';
    }

    const normalized = country
      .replace(/United States/i, 'USA')
      .replace(/United Kingdom/i, 'UK')
      .replace(/United Arab Emirates/i, 'UAE');

    return `assets/images/flags/${normalized}.png`;
  }

  ngOnInit(): void {
    if (this.race) {
      this.raceData = this.race;
    } else {
      this.raceData = this.f1Service.getRaceData();
    }

    if (this.raceData && this.raceData.season && this.raceData.round) {
      this.f1Service.getRaceResults(
        this.raceData.season,
        this.raceData.round
      ).subscribe(result => {
        this.raceResult = result;
      });
    }
  }

}
