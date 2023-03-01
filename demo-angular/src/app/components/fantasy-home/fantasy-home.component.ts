import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/common/user';
import { RegistrationService } from 'src/app/services/registration.service';

@Component({
  selector: 'app-fantasy-home',
  templateUrl: './fantasy-home.component.html',
  styleUrls: ['./fantasy-home.component.css']
})
export class FantasyHomeComponent implements OnInit {
  user! : User;

  constructor(private registerService: RegistrationService,
              private route: Router) {
   }

  ngOnInit(): void {
    this.registerService.getUser().subscribe(user => {
      this.user = user;
      console.log(this.user)
    });
  }

  logout() {
    localStorage.removeItem('userToken');
    this.route.navigate(['/login']);
  }
  }
