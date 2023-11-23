import { DatePipe, Time } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/_services/auth.service';
import { DateTimeServiceService } from 'src/app/_services/date-time-service.service';
import { TokenStorageService } from 'src/app/_services/token-storage.service';
import { UserService } from 'src/app/_services/user.service';
import { DateTimeResponse } from 'src/app/model/DateTimeResponse';
import * as moment from 'moment';
import { Formula1Driver, Formula1Drivers } from 'src/app/model/Formula1Drivers';
import { SipnnerService } from 'src/app/_services/SpinnerService';
import { PredictService } from 'src/app/_services/predict.service';
import { Prediction } from 'src/app/model/Prediction';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private userService: UserService, private authService: AuthService,
    private dateTimeService: DateTimeServiceService, private loadingService: SipnnerService,
    private predictService: PredictService ) { }

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
  predictionLocked : Boolean = false;
  first: number = 0;
  second: number = 0;
  third: number = 0;

  //results
  userHasPrediction: Boolean = false;
  pFirst: string = "";
  pSecond: string = "";
  pThird: string = "";


  ngOnInit(): void {
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
      if (userString) {
        this.user = userString.username;
      }
      //getting all drivers
      this.drivers = Formula1Drivers;
      //getting info for nexRace
      this.dateTimeService.getNextRaceInfo(this.user).subscribe(response => {
        this.raceDate = response.time;
        this.nameRace = response.nameRace;
        this.round = response.round;
        this.predictionLocked = response.predictionLocked;
        if (response.userHavePrediction) {
          this.userHasPrediction = true;
          this.pFirst = response.first;
          this.pSecond = response.second;
          this.pThird = response.third;
        }
        
        //testCoundtow
        this.formattedDate = new Date(this.raceDate);
        console.log(response);

      }, 
      error => {
        this.errorMessage = error.error.message;
        console.log(error.error.message);
      });
      
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
        this.removeDriverFromSelection(selectDriver);
      } else {
        // Otherwise, assign the appropriate selection (1st, 2nd, or 3rd)
        this.drivers[index].selection = numberOfSelections + 1;
        this.addDriverToSelection(selectDriver, numberOfSelections + 1);
      }
    }
  }

  addDriverToSelection(driver: Formula1Driver, position: number) {
    switch (position) {
      case 1:
        this.first = driver.number;
        break;
      case 2:
        this.second = driver.number;
        break;
      case 3:
        this.third = driver.number;
        break;
      default:
        // Handle unexpected position
        break;
    }
  }
  removeDriverFromSelection(driver: Formula1Driver) {
    if (this.first === driver.number) {
    this.first = 0;
  } else if (this.second === driver.number) {
    this.second = 0;
  } else if (this.third === driver.number) {
    this.third = 0;
  }
  }

  resetPredictions(): void {
    // Reset selections for all drivers
    this.drivers.forEach(driver => {
      driver.selection = undefined;
    });
  }

  areAllDriversSelected(): boolean {
    //filters only the drivers that have the selectionProperty not null
    return this.drivers.filter(driver => driver.selection !== undefined).length === 3;
  }

  savePredictions() {
    this.saveDriversToPredict = this.drivers.filter(driver => driver.selection !== undefined);
    const selectedDriverNumbers = this.saveDriversToPredict.map(driver => driver.number);

    this.predictService.savePrediction(this.first, this.second, this.third, this.user, this.round)
    .subscribe(response => {
      this.pFirst = response.first;
      this.pSecond = response.second;
      this.pThird = response.third
    }, error => {
      console.log(error);
      this.errorMessage = error.error.message;
    });
  }

}

