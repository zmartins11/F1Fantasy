import { DatePipe, Time } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, EventEmitter, NgZone, OnInit, Output, Renderer2, ViewChild } from '@angular/core';
import { AuthService } from 'src/app/_services/auth.service';
import { DateTimeServiceService } from 'src/app/_services/date-time-service.service';
import { TokenStorageService } from 'src/app/_services/token-storage.service';
import { UserService } from 'src/app/_services/user.service';
import { DateTimeResponse } from 'src/app/model/DateTimeResponse';
import moment from 'moment';
import { Formula1Driver, Formula1Drivers } from 'src/app/model/Formula1Drivers';
import { SipnnerService } from 'src/app/_services/SpinnerService';
import { PredictService } from 'src/app/_services/predict.service';
import { Prediction } from 'src/app/model/Prediction';
import { faArrowDown, faArrowUp, faGaugeSimpleMed } from '@fortawesome/free-solid-svg-icons'
import { TotalPointsResponse } from 'src/app/model/TotalPointsResponse';
import { PointsInfo } from 'src/app/model/PointsInfo';
import { Standings } from 'src/app/model/Standings';
import { Observable } from 'rxjs';
import { DomSanitizer } from '@angular/platform-browser';
import { NextRaceInfo } from 'src/app/model/NextRaceInfo';
import { Router } from '@angular/router';
import { RaceDetailsComponent } from '../race-details/race-details.component';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { YoutubeService } from 'src/app/_services/youtube.service';
import { WeatherService } from 'src/app/_services/weather.service';
import { WeatherComponent } from '../weather/weather.component';
import { Weather } from 'src/app/model/Weather';
import { F1Service } from 'src/app/_services/f1.service';
import { Driver } from 'src/app/model/Driver';



@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css', './home.component.align.css']
})
export class HomeComponent implements OnInit {

  constructor(private userService: UserService, private authService: AuthService,
    private dateTimeService: DateTimeServiceService, private spinnerService: SipnnerService,
    private predictService: PredictService,
    private sanitizer: DomSanitizer,
    private router: Router,
    private modalService: BsModalService,
    private youtubeService: YoutubeService,
    private weatherService: WeatherService,
    private f1Service: F1Service,
    private renderer: Renderer2
  ) { }

  // Reference to the modal element
  @ViewChild('myModal') myModal!: ElementRef;


  content: String | undefined;
  isLoggedIn: Boolean = false;
  user: string = '';
  userId: number | null = null;
  raceDate: string = '';
  round: string = '';
  timeRemaining: any;
  nameRace: string = '';
  country: string = '';
  city: string = '';
  drivers: Driver[] = [];
  driverMap = new Map<number, Driver>();
  saveDriversToPredict: Driver[] = [];
  showDrivers = false;
  showDriversFastest = false;
  errorMessage = null;
  sessionExpiredMessage: string = '';
  errorMessagePopUp: string = "";
  successMessage: string = "";
  IncompletePredictionMessage: string = "";
  formattedDate: Date | any;
  isLoading = false;
  predictionLocked: Boolean = false;
  first: number = 0;
  second: number = 0;
  third: number = 0;
  fastest: number = 0;
  showAlert: boolean = false;
  showAlertSuccess: boolean = false;
  showAlertIncomplete: boolean = false;
  isHighlighted: boolean = false;
  totalPointsData: TotalPointsResponse[] | null = null;
  driversStandings: TotalPointsResponse[] | null = null;
  constructorStandings: TotalPointsResponse[] | null = null;
  safeUrl: any;
  bsModalRef: BsModalRef | undefined;
  currentSeason = new Date().getFullYear();

  pointsInfo: PointsInfo[] = [];
  showPopUpDriversPoints = false;
  totalPoints: number = 0;
  highlightedDriver: Driver | null = null;
  currentTable: string = 'userPoints';
  currentPredictionCard: string = 'podium';
  totalLength: any;
  page: number = 1;



  //results
  pFirst: string | undefined = "";
  pSecond: string | undefined = "";
  pThird: string | undefined = "";
  pFastestLap: string | undefined = "";
  pHasPodium: Boolean = false;
  pHasFastestLap: Boolean = false;
  @Output() darkModeChange = new EventEmitter<boolean>();
  darkMode: boolean = false;
  faArrowDown = faArrowDown;
  faArrowUp = faArrowUp;
  events!: any;
  videoId: string = "";
  weatherData: Weather | null = null;
  raceDay: number = 0;
  raceMonth: number = 0;
  raceHour: number = 0;





  ngOnInit(): void {
    this.updateCountdown();
    setInterval(() => {
      this.updateCountdown();
    }, 1000)
    if (this.authService.getToken()) {
      this.isLoggedIn = true;
      const userString = this.authService.getUser();
      if (userString) {
        this.user = userString.username;
        this.userId = userString.userId;
      }


      //getting all drivers backend api
      this.f1Service.getDriversList(this.currentSeason).subscribe(drivers => {
        this.drivers = drivers;
        this.driverMap.clear();

        drivers.forEach(driver => {
          this.driverMap.set(driver.permanentNumber, driver);
        });
      });

      //getting info for nexRace
      if (this.userId === null) {
        this.authService.signOut();
        this.router.navigate(['/login']);
        return;
      }

      this.dateTimeService.getNextRaceInfo(this.userId).subscribe(response => {
        this.raceDate = response.time;
        this.nameRace = response.nameRace;
        this.round = response.round;
        this.country = response.country;
        this.city = response.city;
        this.predictionLocked = response.predictionLocked;
        const date = new Date(response.raceDate);


        this.raceDay = date.getDate();
        this.raceMonth = date.getMonth() + 1;
        //ajust timezone
        this.raceHour = parseInt(response.raceTime, 10);
        this.populateWeatherWidget();
        if (response.predictedPodium) {
          this.pHasPodium = true;
          this.pFirst = this.getDriverName(Number(response.first));
          this.pSecond = this.getDriverName(Number(response.second));
          this.pThird = this.getDriverName(Number(response.third));
          if (response.predictedFastestLap) {
            this.pHasFastestLap = true;
            this.pFastestLap = this.getDriverName(Number(response.fastestLap));
          }
        }
        //testCoundtow
        this.formattedDate = new Date(this.raceDate);
      },
        error => {
          this.showAlert = true;
          this.errorMessage = error.error?.message || 'Unable to load the next race.';
          if (error.status !== 401) {
            return;
          }
          let countdown = 3; // Set the initial countdown value

          const intervalId = setInterval(() => {
            this.sessionExpiredMessage = `Your session has expired. You will be logged out in ${countdown} seconds...`;
            countdown--;

            if (countdown === 0) {
              clearInterval(intervalId);

              // Redirect to login page
              this.authService.signOut();
              window.location.reload();
              this.router.navigate(['/home']);
            }
          }, 1000); // Update the countdown every second
      });


      this.getPointsInfo();
      this.populatePointsTables();
      //this.getYTBVideo();
      this.getStandings();
    }

    const storedTheme = localStorage.getItem('homeDarkMode');
    if (storedTheme !== null) {
      this.darkMode = JSON.parse(storedTheme);
    }
  }

  private getDriverName(permanentNumber: number): string | undefined {
    return this.drivers.find(driver =>
      driver.permanentNumber === permanentNumber
    )?.familyName;
  }


  getStandings() {
    this.dateTimeService.getStandingsSeason().subscribe(response => {
      this.driversStandings = response.drivers;
      this.constructorStandings = response.constructors;
    })
  }

  emitDarkModeToggle(): void {
    const body = document.body;
    if (this.darkMode) {
      this.renderer.addClass(body, 'dark-mode');
    } else {
      this.renderer.removeClass(body, 'dark-mode');
    }
    this.darkModeChange.emit(this.darkMode);
  }

  getYTBVideo() {
    this.youtubeService.searchVideo("formula 1 monaco 2023 highlights tsn").subscribe(
      (response: any) => {
        if (response.videoId) {
          this.videoId = response.videoId;
          console.log('Video ID:', this.videoId);
          // Now, this.videoId contains the retrieved videoId
        } else {
          console.error('Error:', response.error);
          // Handle the error if needed
        }
      },
      (error) => {
        console.error('HTTP Request Error:', error);
        // Handle the error if needed
      }
    );
  }

  populatePointsTables() {
    //populate tables
    this.dateTimeService.getTotalPoints(this.user).subscribe(response => {
      if (Array.isArray(response)) {
        this.totalPointsData = response;

        // Check if the length is less than 6
        if (this.totalPointsData.length < 6) {
          // Calculate how many default objects to add
          const remainingCount = 6 - this.totalPointsData.length;

          // Add default objects to the array
          for (let i = 0; i < remainingCount; i++) {
            const defaultObject: TotalPointsResponse = {
              position: '-',
              username: '---',
              points: '-'
            };

            this.totalPointsData.push(defaultObject);
          }
        }
      }
    }, error => {
      this.errorMessage = error.error.message;
    });
  }

  getPointsInfo() {
    //get info points
    if (this.userId === null) {
      return;
    }

    this.dateTimeService.getPointsInfo(this.userId).subscribe(response => {
      this.pointsInfo = response || [];
      if (this.pointsInfo.length !== 0) {
        this.showPopUpDriversPoints = true;
        this.calculateTotalPoints();
        this.openModal();
      } else {
        this.showPopUpDriversPoints = false;
        console.log("dont showpop")
      }
    }, error => {
      this.showPopUpDriversPoints = false;
      console.error('Error fetching pointsInfo:', error);
    });
  }

  populateWeatherWidget() {
    this.weatherService.getWeather(this.country, this.city, this.raceHour, this.raceDay, this.raceMonth, false).subscribe(
      (response: Weather[]) => {
        if (Array.isArray(response) && response.length > 0) {
          const weatherInfo = response[0];

          this.weatherData = {
            date: weatherInfo.date,
            weather: weatherInfo.weather,
            temperature: weatherInfo.temperature,
            humidity: weatherInfo.humidity,
            wind_speed: weatherInfo.wind_speed,
            current_weather: weatherInfo.current_weather
          };
          console.log(this.weatherData);
        }
      },
      error => {
        console.error('Error fetching weather data:', error.message || error);
      }
    );
  }

  openRacePopup() {
    this.dateTimeService.getNextRaceDetails().subscribe(race => {
      const initialState = {
        race: race
      };

      const modalOptions = {
        initialState,
        class: 'modal-lg',
      };

      this.bsModalRef = this.modalService.show(RaceDetailsComponent, modalOptions);
    },
      error => {
        let countdown = 3; // Set the initial countdown value

        const intervalId = setInterval(() => {
          this.errorMessagePopUp = `Your session has expired. You will be logged out in ${countdown} seconds...`;
          countdown--;
          if (countdown === 0) {
            clearInterval(intervalId);

            // Redirect to login page
            this.authService.signOut();
            window.location.reload();
            this.router.navigate(['/home']);
          }
        }, 1000);
      }
    );
  }





  getDriversStandings(): TotalPointsResponse[] {
    return this.driversStandings || [];
  }

  getConstructoresStandings(): TotalPointsResponse[] {
    return this.constructorStandings || [];
  }

  calculateTotalPoints() {
    this.totalPoints = this.pointsInfo.reduce((total, pointInfo) => total + pointInfo.points, 0);
  }

  openModal() {
    const modal: any = this.myModal.nativeElement;
    modal.style.display = 'block';
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

  selectDriver(selectDriver: Driver) {

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

  selectFastestDriver(selectDriver: Driver) {
    this.fastest = selectDriver.permanentNumber;
  }

  addDriverToSelection(driver: Driver, position: number) {
    switch (position) {
      case 1:
        this.first = driver.permanentNumber;
        break;
      case 2:
        this.second = driver.permanentNumber;
        break;
      case 3:
        this.third = driver.permanentNumber;
        break;
      default:
        // Handle unexpected position
        break;
    }
  }
  removeDriverFromSelection(driver: Driver) {
    if (this.first === driver.permanentNumber) {
      this.first = 0;
    } else if (this.second === driver.permanentNumber) {
      this.second = 0;
    } else if (this.third === driver.permanentNumber) {
      this.third = 0;
    }
  }

  resetPredictions(): void {
    // Reset selections for all drivers
    this.drivers.forEach(driver => {
      driver.selection = undefined;
    });
    this.first = 0;
    this.second = 0;
    this.third = 0;
  }

  areAllDriversSelected(): boolean {
    //filters only the drivers that have the selectionProperty not null
    return this.drivers.filter(driver => driver.selection !== undefined).length === 3;
  }

  savePredictions() {
    this.saveDriversToPredict = this.drivers.filter(driver => driver.selection !== undefined);

    if (this.userId === null) {
      return;
    }

    this.predictService.savePrediction(this.first, this.second, this.third, this.fastest, this.userId, Number(this.round), this.currentSeason)
      .subscribe(response => {
        this.pFirst = this.getDriverName(Number(response.first));
        this.pSecond = this.getDriverName(Number(response.second));
        this.pThird = this.getDriverName(Number(response.third));
        this.pFastestLap = this.getDriverName(Number(response.fastestLap));
        this.resetPredictions();
        this.showDrivers = false;
        this.showDriversFastest = false;
        this.successMessage = "Your prediction has been saved!";
        this.showSuccessAlert("Your prediction has been saved!");
        this.showAlertSuccess = true;
        this.showAlert = true;
        if (response.predictedPodium == false || response.predictedFastestLap == false) {
          let tempMessage = "";
          if (response.predictedPodium == false) {
            tempMessage = "podium";
            this.pHasPodium = false;
            this.pHasFastestLap = true;
          } else {
            tempMessage = "fastestLap";
            this.pHasPodium = true;
          } if (response.predictedFastestLap) {
            this.pHasFastestLap = true;
          }
          this.IncompletePredictionMessage = "To get more points, fill the " + tempMessage + " prediction!";
          this.showAlertSuccess = true;
          this.showAlertIncomplete = true;
        }
        //check if fastestLap
      }, error => {
        this.errorMessage = error.error.message;
        let countdown = 3; // Set the initial countdown value
        const intervalId = setInterval(() => {
          this.sessionExpiredMessage = `Your1 session has expired. You will be logged out in ${countdown} seconds...`;
          countdown--;

          if (countdown === 0) {
            clearInterval(intervalId);

            // Redirect to login page
            this.authService.signOut();
            window.location.reload();
            this.router.navigate(['/home']);
          }
        }, 1000); // Update the countdown every second
      });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }



  showAllDrivers() {
    return this.drivers;
  }

  closeAlert(alertType: string) {
    if (alertType === "success") {
      setTimeout(() => {
        this.showAlertSuccess = false;
      }, 1000); // Adjust the duration to match the transition duration
    } else {
      setTimeout(() => {
        this.showAlertIncomplete = false;
      }, 1000); // Adjust the duration to match the transition duration
    }
  }


  closePopup() {
    this.showPopUpDriversPoints = false;
    const modal: any = this.myModal.nativeElement;
    modal.style.display = 'none';
  }

  highlightDriver(driver: Driver) {
    this.highlightedDriver = driver;
  }

  unhighlightDriver(driver: Driver) {
    if (this.highlightedDriver === driver) {
      this.highlightedDriver = null;
    }
  }

  switchTable(table: string): void {
    this.currentTable = table;
    this.page = 1;
  }

  switchPredictionCard(card: string) {
    this.currentPredictionCard = card;
  }

  highlightText() {
    this.isHighlighted = true;
  }

  removeHighlight() {
    this.isHighlighted = false;
  }

  showSuccessAlert(message: string): void {
    this.successMessage = message;
    this.showAlertSuccess = true;

    // Close the alert after 3 seconds
    setTimeout(() => {
      this.closeAlert('success');
    }, 3000);
  }

}

