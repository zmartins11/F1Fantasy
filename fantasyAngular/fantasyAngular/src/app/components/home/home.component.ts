import { DatePipe, Time } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, NgZone, OnInit, ViewChild } from '@angular/core';
import { AuthService } from 'src/app/_services/auth.service';
import { UserService } from 'src/app/_services/user.service';
import moment from 'moment';
import { SipnnerService } from 'src/app/_services/SpinnerService';
import { PredictService } from 'src/app/_services/predict.service';
import { faArrowDown, faArrowUp, faGaugeSimpleMed, faLock } from '@fortawesome/free-solid-svg-icons'
import { TotalPointsResponse } from 'src/app/model/TotalPointsResponse';
import { PointsInfo } from 'src/app/model/PointsInfo';
import { Observable } from 'rxjs';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { RaceDetailsComponent } from '../race-details/race-details.component';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { YoutubeService } from 'src/app/_services/youtube.service';
import { WeatherService } from 'src/app/_services/weather.service';
import { WeatherComponent } from '../weather/weather.component';
import { Weather } from 'src/app/model/Weather';
import { F1Service } from 'src/app/_services/f1.service';
import { Driver } from 'src/app/model/Driver';
import { TotalPointsUsers } from 'src/app/model/TotalPointsUsers';
import { ScoringService } from 'src/app/_services/scoring.service';



@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css', './home.component.align.css']
})
export class HomeComponent implements OnInit {

  constructor(private userService: UserService, private authService: AuthService,
    private scoringService: ScoringService, private spinnerService: SipnnerService,
    private predictService: PredictService,
    private sanitizer: DomSanitizer,
    private router: Router,
    private modalService: BsModalService,
    private youtubeService: YoutubeService,
    private weatherService: WeatherService,
    private f1Service: F1Service
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
  totalPointsData: TotalPointsUsers[] | null = null;
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
  faArrowDown = faArrowDown;
  faArrowUp = faArrowUp;
  faLock = faLock;
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
        console.log("DRIVERS:", this.drivers);

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

      this.predictService.getNextRaceInfo().subscribe(response => {
        this.raceDate = response.race.time;
        this.nameRace = response.race.nameRace;
        this.round = response.race.round;
        this.country = response.race.country;
        this.city = response.race.city;
        this.predictionLocked = response.race.predictionLocked;
        const date = new Date(response.race.raceDate);


        this.raceDay = date.getDate();
        this.raceMonth = date.getMonth() + 1;
        //ajust timezone
        this.raceHour = parseInt(response.race.raceTime, 10);
        this.populateWeatherWidget();
        if (response.prediction.predictedPodium) {
          this.pHasPodium = true;
          this.pFirst = this.getDriverName(Number(response.prediction.first));
          this.pSecond = this.getDriverName(Number(response.prediction.second));
          this.pThird = this.getDriverName(Number(response.prediction.third));
          if (response.prediction.predictedFastestLap) {
            this.pHasFastestLap = true;
            this.pFastestLap = this.getDriverName(Number(response.prediction.fastestLap));
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

    if (!this.isLoggedIn) {
      this.currentTable = 'driverStandings';
      this.getStandings();
      this.loadPublicNextRace();
    }
  }

  private getDriverName(permanentNumber: number): string | undefined {
    return this.drivers.find(driver =>
      driver.permanentNumber === permanentNumber
    )?.familyName;
  }


  getStandings() {
    this.f1Service.getStandings().subscribe(response => {
      this.driversStandings = response.drivers;
      this.constructorStandings = response.constructors;
    })
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
    this.scoringService.getTotalPoints().subscribe(response => {
      if (Array.isArray(response)) {
        this.totalPointsData = response;

        // Check if the length is less than 6
        if (this.totalPointsData.length < 6) {
          // Calculate how many default objects to add
          const remainingCount = 6 - this.totalPointsData.length;

          // Add default objects to the array
          for (let i = 0; i < remainingCount; i++) {
            const defaultObject: TotalPointsUsers = {
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

    this.scoringService.getPointsInfo().subscribe(response => {
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
            windSpeed: weatherInfo.windSpeed,
            currentWeather: weatherInfo.currentWeather
          };
          console.log(this.weatherData);
        }
      },
      error => {
        console.error('Error fetching weather data:', error.message || error);
      }
    );
  }

  private loadPublicNextRace(): void {
    this.f1Service.getNextRaceDetails().subscribe(race => {
      const raceTime = race.time || '00:00:00';
      const raceMoment = moment(`${race.date}T${raceTime}`);

      this.raceDate = raceMoment.format('YYYYMMDDHHmm');
      this.nameRace = race.raceName;
      this.round = race.round;
      this.country = race.Circuit.Location.country;
      this.city = race.Circuit.Location.locality;
      this.raceDay = raceMoment.date();
      this.raceMonth = raceMoment.month() + 1;
      this.raceHour = parseInt(raceTime, 10) || 0;
      this.predictionLocked = false;
      this.formattedDate = raceMoment.toDate();
      this.populateWeatherWidget();
    }, error => {
      console.error('Error fetching the next race:', error.message || error);
    });
  }

  openRacePopup() {
    this.f1Service.getNextRaceDetails().subscribe(race => {
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

    const index = this.drivers.findIndex(driver => driver === selectDriver);
    if (index === -1) {
      return;
    }

    if (this.drivers[index].selection !== undefined) {
      // Already selected -> toggle it off
      this.drivers[index].selection = undefined;
    } else {
      const numberOfSelections = this.drivers.filter(driver => driver.selection !== undefined).length;
      if (numberOfSelections >= 3) {
        return;
      }
      this.drivers[index].selection = numberOfSelections + 1;
    }

    this.resequenceSelections();
  }

  // Keep selection values contiguous (1,2,3) and sync first/second/third
  private resequenceSelections() {
    const selected = this.drivers
      .filter(driver => driver.selection !== undefined)
      .sort((a, b) => (a.selection ?? 0) - (b.selection ?? 0));

    selected.forEach((driver, i) => {
      driver.selection = i + 1;
    });

    this.first = selected[0] ? selected[0].permanentNumber : 0;
    this.second = selected[1] ? selected[1].permanentNumber : 0;
    this.third = selected[2] ? selected[2].permanentNumber : 0;
  }

  selectFastestDriver(selectDriver: Driver) {
    this.fastest = selectDriver.permanentNumber;
  }

  // Display the pending selection if it exists, otherwise the saved prediction
  get displayFirst(): string | undefined {
    return this.first ? this.getDriverName(this.first) : this.pFirst;
  }

  get displaySecond(): string | undefined {
    return this.second ? this.getDriverName(this.second) : this.pSecond;
  }

  get displayThird(): string | undefined {
    return this.third ? this.getDriverName(this.third) : this.pThird;
  }

  get displayFastestLap(): string | undefined {
    return this.fastest ? this.getDriverName(this.fastest) : this.pFastestLap;
  }

  // Names of the pending (not yet saved) podium selections, for the picker progress slots
  get pendingFirst(): string {
    return this.first ? this.getDriverName(this.first) ?? '' : '';
  }

  get pendingSecond(): string {
    return this.second ? this.getDriverName(this.second) ?? '' : '';
  }

  get pendingThird(): string {
    return this.third ? this.getDriverName(this.third) ?? '' : '';
  }

  teamColor(driver: Driver): string {
    const colors: { [key: string]: string } = {
      red_bull: '#3671c6',
      racing_bulls: '#6692ff',
      rb: '#6692ff',
      ferrari: '#e8002d',
      mercedes: '#27f4d2',
      mclaren: '#ff8000',
      aston_martin: '#229971',
      alpine: '#0093cc',
      williams: '#64c4ff',
      sauber: '#52e252',
      audi: '#52e252',
      haas: '#b6babd',
      cadillac: '#c8a951'
    };
    return colors[driver.constructorId] || '#64748b';
  }

  // Soft translucent variant of the team color for chip backgrounds
  teamColorSoft(driver: Driver): string {
    return this.teamColor(driver) + '26';
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

