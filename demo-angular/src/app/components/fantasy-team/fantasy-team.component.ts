import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Driverf } from 'src/app/common/fantasy/driverf';
import { FantasyTeam } from 'src/app/common/fantasy/fantasyTeam';
import { Team } from 'src/app/common/fantasy/teamf';
import { FantasyService } from 'src/app/services/fantasyService';
import { NumberLiteralType } from 'typescript';

@Component({
  selector: 'app-fantasy-team',
  templateUrl: './fantasy-team.component.html',
  styleUrls: ['./fantasy-team.component.css']
})
export class FantasyTeamComponent implements OnInit {

  constructor(private fantasyService: FantasyService,
              private route: Router) { }

  fantasyTeam: any;
  driverList: Driverf[] = [];
  constructorList: Team[] = [];
  showDriverTable = true;

  ngOnInit(): void {
    this.fantasyTeam = this.fantasyService.fantasyTeam;
    this.getAllDrivers();
    this.getAllConstructor();
  }

  getAllDrivers() {
    this.fantasyService.getAllDrivers().subscribe((data: Driverf[]) => {
      this.driverList = data;
    });
  }

  getAllConstructor() {
    this.fantasyService.getAllConstructors().subscribe((data : Team[]) => {
      this.constructorList = data;
    })
  }

  toggleTable() {
    this.showDriverTable = !this.showDriverTable;
  }


  onDeleteDriver(driverNumber: number) {
    this.fantasyTeam[`driver${driverNumber}`].photoUrl = 'assets/images/drivers/default.png';
    this.fantasyTeam[`driver${driverNumber}`].name = '';
    this.fantasyTeam[`driver${driverNumber}`].id = 0;
    this.fantasyTeam[`driver${driverNumber}`].defaultImageUsed = true;
    this.fantasyTeam.budget = this.fantasyTeam.budget + this.fantasyTeam[`driver${driverNumber}`].price;
  }

  onDeleteConstructor(team : Team) {
    team.id = 0;
    team.name = '';
    team.photoUrl = 'assets/images/fantasy/teams/default.png';
    team.price = 0;
  }

  onUpdateDriver(driver: Driverf, team: FantasyTeam) {
    const errorMessageElement = document.getElementById('error-message')!;
    errorMessageElement.innerHTML = '';

    if (team.driver1.id == 0) {
      if (team.driver2.id == driver.id || team.driver3.id == driver.id) {
        errorMessageElement.innerHTML = 'Driver already exists in team';
      } else {
        team.driver1.photoUrl = driver.photoUrl;
        team.driver1.id = driver.id;
        team.driver1.name = driver.name;
        team.budget = team.budget - driver.price;
      }
    }
    if (team.driver2.id == 0) {
      if (team.driver1.id == driver.id || team.driver3.id == driver.id) {
        errorMessageElement.innerHTML = 'Driver already exists in team';
      } else {
        team.driver2.photoUrl = driver.photoUrl;
        team.driver2.id = driver.id;
        team.driver2.name = driver.name;
        team.budget = team.budget - driver.price;
      }
    }
    if (team.driver3.id == 0) {
      if (team.driver2.id == driver.id || team.driver1.id == driver.id) {
        errorMessageElement.innerHTML = 'Driver already exists in team';
      } else {
        team.driver3.photoUrl = driver.photoUrl;
        team.driver3.id = driver.id;
        team.driver3.name = driver.name;
        team.budget = team.budget - driver.price;
      }
    }

    
  }

  saveTeam(fantasyTeam: FantasyTeam) {
    const errorMessageElement = document.getElementById('error-message')!;
    console.log(fantasyTeam.budget);
    if(fantasyTeam.budget < 0) {
      errorMessageElement.innerHTML = 'check budget!';
    } else {
      this.fantasyService.updateTeam(fantasyTeam).subscribe(
        response => this.route.navigate(['/fantasy-home']), 
        error => errorMessageElement.innerHTML = 'error Updating team');
    }
  }


}
