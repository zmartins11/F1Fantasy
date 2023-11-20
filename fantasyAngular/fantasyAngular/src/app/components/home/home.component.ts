import { DatePipe, Time } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/_services/auth.service';
import { DateTimeServiceService } from 'src/app/_services/date-time-service.service';
import { TokenStorageService } from 'src/app/_services/token-storage.service';
import { UserService } from 'src/app/_services/user.service';
import { DateTimeResponse } from 'src/app/model/DateTimeResponse';
import * as moment from 'moment';
import { Formula1Driver, Formula1Drivers } from 'src/app/model/Formula1Drivers';
import { LoadingService } from 'src/app/_services/loading.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private userService: UserService, private authService: AuthService,
    private dateTimeService: DateTimeServiceService, private loadingService: LoadingService) { }

  content: String | undefined;
  isLoggedIn: Boolean = false;
  user: string = '';
  raceDate: string =  '';
  round : string = '';
  timeRemaining : any;
  nameRace : string  = '';
  drivers: Formula1Driver[]= [];
  saveDriversToPredict : Formula1Driver[] = [];
  showDrivers = false;
  errorMessage = null;
  formattedDate: Date | any;
  isLoading = false;

  ngOnInit(): void {
    this.loadingService.isLoading$.subscribe((isLoading) => {
      this.isLoading = isLoading;
    })
    this.updateCountdown();
    setInterval(() => {
      this.updateCountdown();
    }, 1000)
    this.userService.getPublicContent().subscribe(
      data => {
        this.content = data;
      },
      err => {
        this.content = err.error.message;
        console.log(err.message);
      }
    );
    if (this.authService.getToken()) {
      this.isLoggedIn = true;
      const userString = this.authService.getUser();
      //getting all drivers
      this.drivers = Formula1Drivers;
      //getting info for nexRace
      this.dateTimeService.getNextRaceInfo().subscribe(response => {
        this.raceDate = response.time;
        this.nameRace = response.nameRace;
        this.round = response.round;
        //testCoundtow
        this.formattedDate = new Date(this.raceDate);
        console.log(response);

      }, 
      error => {
        this.errorMessage = error.error.message;
        console.log(error.error.message);
      });
      if (userString) {
        this.user = userString.username;
      }
    }
  } 
  updateCountdown() {
    const currentDate = moment();
    const targetDate = moment(this.raceDate, 'YYYYMMDDHHmm');
    const duration = moment.duration(targetDate.diff(currentDate));

    this.timeRemaining = {
      days: duration.days(),
      hours: duration.hours(),
      minutes: duration.minutes(),
      seconds: duration.seconds()
    };
  }

  selectDriver(selectDriver : Formula1Driver) {
    
    const numberOfSelections = this.drivers.filter(driver => driver.selection).length;

    if (numberOfSelections < 3) {
      // Find the index of the selected driver in the array
      const index = this.drivers.findIndex(driver => driver === selectDriver);

      // Update the selection for the clicked driver
      if (this.drivers[index].selection) {
        // If already selected, remove the selection (if driver is already selectd this if removes the selectd driver)
        this.drivers[index].selection = undefined;
      } else {
        // Otherwise, assign the appropriate selection (1st, 2nd, or 3rd)
        this.drivers[index].selection = numberOfSelections + 1;
      }
    }
    console.log(selectDriver);
  }

  resetPredictions(): void {
    // Reset selections for all drivers
    this.drivers.forEach(driver => {
      console.log(driver);
      driver.selection = undefined;
    });
  }

  areAllDriversSelected(): boolean {
    //filters only the drivers that have the selectionProperty not null
    return this.drivers.filter(driver => driver.selection !== undefined).length === 3;
  }

  savePredictions() {
    this.saveDriversToPredict = this.drivers.filter(driver => driver.selection !== undefined);
    console.log(this.saveDriversToPredict);
    console.log(this.authService.getUser()?.username);

    //TODO : nextRaceInfo object : add the round of the race
  }



}