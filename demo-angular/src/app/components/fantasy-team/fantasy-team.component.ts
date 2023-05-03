import { Constructor } from '@angular/cdk/table';
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
  initialBudget : number = 0;
  currentBudget : number = 0;

  ngOnInit(): void {
    this.fantasyTeam = this.fantasyService.fantasyTeam;
    this.getAllDrivers();
    this.getAllConstructor();
    this.initialBudget = this.fantasyTeam.budget;
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
    this.fantasyTeam.budget = this.initialBudget + this.fantasyTeam[`driver${driverNumber}`].price;
  }

  onDeleteConstructor(team : Team) {
    console.log(team);
    this.fantasyTeam.team.id = 0;
    this.fantasyTeam.team.name = '';
    this.fantasyTeam.team.photoUrl = 'assets/images/fantasy/teams/default.png';
    this.fantasyTeam.budget = this.initialBudget + this.fantasyTeam.team.price;
  }

  onUpdateDriver(driver: Driverf, team: FantasyTeam) {
    const errorMessageElement = document.getElementById('error-message')!;
    errorMessageElement.innerHTML = '';
    

    if (team.driver1.id == 0) {
      this.currentBudget = team.budget - driver.price;
      if (team.driver2.id == driver.id || team.driver3.id == driver.id || this.currentBudget <0) {
        errorMessageElement.innerHTML = 'Driver already exists in team or no budget';
      } else {
        team.driver1.photoUrl = driver.photoUrl;
        team.driver1.id = driver.id;
        team.driver1.name = driver.name;
        team.budget = team.budget - driver.price;
      }
    }
    if (team.driver2.id == 0) {
      this.currentBudget = team.budget - driver.price;
      if (team.driver1.id == driver.id || team.driver3.id == driver.id || this.currentBudget <0) {
        errorMessageElement.innerHTML = 'Driver already exists in team or no budget';
      } else {
        team.driver2.photoUrl = driver.photoUrl;
        team.driver2.id = driver.id;
        team.driver2.name = driver.name;
        team.budget = team.budget - driver.price;
      }
      
    }
    if (team.driver3.id == 0) {
      this.currentBudget = team.budget - driver.price;
      if (team.driver2.id == driver.id || team.driver1.id == driver.id || this.currentBudget <0) {
        errorMessageElement.innerHTML = 'Driver already exists in team or no budget';
      } else {
        team.driver3.photoUrl = driver.photoUrl;
        team.driver3.id = driver.id;
        team.driver3.name = driver.name;
        team.budget = team.budget - driver.price;
      } 
    }
  }

  onUpdateTeam(constructor : Team, team: FantasyTeam) {
    const errorMessageElement = document.getElementById('error-message')!;
    errorMessageElement.innerHTML = '';
    this.currentBudget = team.budget - constructor.price;

    if(team.team.id == 0 && this.currentBudget>0) {
      team.team.id = constructor.id;
      team.team.name = constructor.name;
      team.team.photoUrl = constructor.photoUrl
      team.budget = team.budget - constructor.price;
    } else {
      errorMessageElement.innerHTML = 'No budget';
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
