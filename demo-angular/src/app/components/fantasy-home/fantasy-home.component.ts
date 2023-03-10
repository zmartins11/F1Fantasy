import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FantasyTeam } from 'src/app/common/fantasy/fantasyTeam';
import { User } from 'src/app/common/user';
import { FantasyService } from 'src/app/services/fantasyService';
import { RegistrationService } from 'src/app/services/registration.service';

@Component({
  selector: 'app-fantasy-home',
  templateUrl: './fantasy-home.component.html',
  styleUrls: ['./fantasy-home.component.css']
})
export class FantasyHomeComponent implements OnInit {
  user! : User;
  fantasyTeam! : FantasyTeam;

  constructor(private registerService: RegistrationService,
              private route: Router,
              private fantasyService: FantasyService) {
   }

  ngOnInit(): void {
    this.registerService.getUser().subscribe(user => {
      this.user = user;
      console.log(this.user)
      this.getFantasyTeam();
    });

  }

  logout() {
    localStorage.removeItem('userToken');
    this.route.navigate(['/login']);
  }

  goToFantasyTeam() {
    this.route.navigate(['/fantasy-team'])
  }

  getFantasyTeam() {
    console.log(this.user)
    this.fantasyService.getFantasyTeamByUserId(this.user.id).subscribe( fantasyTeam => {
      this.fantasyTeam = fantasyTeam;
      this.fantasyService.fantasyTeam = fantasyTeam;
    })
  }
  }
