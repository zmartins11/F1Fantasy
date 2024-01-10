import { Component, ElementRef, HostListener, OnInit, Renderer2 } from '@angular/core';
import { TokenStorageService } from './_services/token-storage.service';
import { User } from './model/user';
import { Router } from '@angular/router';
import { AuthService } from './_services/auth.service';
import { DateTimeServiceService } from './_services/date-time-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'fantasyAngular';
  private roles: string = '';
  isLoggedIn = false;
  showAdminBoard = false;
  showModeratorBoard = false;
  username: string = '';
  user : User | null | undefined;

  constructor(private authService: AuthService, private router: Router,
              private dateTimeService : DateTimeServiceService,
              private el: ElementRef, private renderer: Renderer2) { }

  ngOnInit(): void {
        this.user= this.authService.getUser();
        if(this.user) {
          this.isLoggedIn = true;
          console.log(this.isLoggedIn);
          this.roles = this.user.role;
          this.showAdminBoard = this.roles.includes('ADMIN');
          this.showModeratorBoard = this.roles.includes('ROLE_MODERATOR');
        }
  }

  logout(): void {
    this.authService.signOut();
    window.location.reload();
  }

  checkLogin(): boolean {
    if(this.authService.getToken()) {
      return true;
    } else {
      return false;
    };
  }

  getUsername(): String {
    this.user = this.authService.getUser();
    if(this.user!= null) {
      return this.username = this.user.username;
    }
    return '';
  }




}
