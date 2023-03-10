import { Component, OnInit } from '@angular/core';
import { FantasyService } from 'src/app/services/fantasyService';

@Component({
  selector: 'app-fantasy-team',
  templateUrl: './fantasy-team.component.html',
  styleUrls: ['./fantasy-team.component.css']
})
export class FantasyTeamComponent implements OnInit {

  constructor(private fantasyService: FantasyService) { }

  fantasyTeam: any;

  ngOnInit(): void {
    this.fantasyTeam = this.fantasyService.fantasyTeam;
    console.log('from team:')
    console.log(this.fantasyTeam.driver1.name)
  }

}
