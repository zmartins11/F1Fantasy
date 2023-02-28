import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/common/user';
import { RegistrationService } from 'src/app/services/registration.service';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {


  user = new User();
  msg = { message: '', status: 0 };
  constructor(private registrationService: RegistrationService,
    private router : Router) { }

  ngOnInit(): void {
  }

  registerUser() {
    this.registrationService.registerUserFromRemote(this.user).subscribe(
      data => {
        console.log("register ok");
        
        this.router.navigate(['/login'])
      }, 
      error => {
        console.log("register not ok");
        this.msg = error.error;
      }
    )
  }

}
