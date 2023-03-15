import { Component, OnInit } from '@angular/core';
import { Driverf } from 'src/app/common/fantasy/driverf';
import { FantasyTeam } from 'src/app/common/fantasy/fantasyTeam';
import { FantasyService } from 'src/app/services/fantasyService';

@Component({
  selector: 'app-fantasy-team',
  templateUrl: './fantasy-team.component.html',
  styleUrls: ['./fantasy-team.component.css']
})
export class FantasyTeamComponent implements OnInit {

  constructor(private fantasyService: FantasyService) { }

  fantasyTeam: any;


  driverList : Driverf [] = []; 

  ngOnInit(): void {
    this.fantasyTeam = this.fantasyService.fantasyTeam;
    this.getAllDrivers();
  }

  getAllDrivers() {
    this.fantasyService.getAllDrivers().subscribe((data: Driverf[]) => {
      this.driverList = data;
    });
  }


  onDeleteDriver(driverNumber : number) {
    this.fantasyTeam[`driver${driverNumber}`].photoUrl = 'assets/images/drivers/default.png';
    this.fantasyTeam[`driver${driverNumber}`].name = '';
    this.fantasyTeam[`driver${driverNumber}`].id = 0;
    this.fantasyTeam[`driver${driverNumber}`].defaultImageUsed = true;
  }

  onUpdateDriverImage(driver : Driverf, team : FantasyTeam) {
    const errorMessageElement = document.getElementById('error-message')!;
   
    if(team.driver1.id == 0 )  {
      if(team.driver2.id != driver.id && team.driver3.id != driver.id) {
        errorMessageElement.innerHTML = 'Driver already exists in team';
      }  else {
      team.driver1.photoUrl = driver.photoUrl;
      team.driver1.id = driver.id;
      team.driver1.name = driver.name;
    }
  }
    if(team.driver2.id == 0) {
      if(team.driver1.id != driver.id && team.driver3.id != driver.id) {
        errorMessageElement.innerHTML = 'Driver already exists in team';
      } else {
        team.driver2.photoUrl = driver.photoUrl;
        team.driver2.id = driver.id;
        team.driver2.name = driver.name;
      }  
  }
  if(team.driver3.id == 0) {
    team.driver3.photoUrl = driver.photoUrl;
    team.driver3.id = driver.id;
    team.driver3.name = driver.name;

}

  }


}
