import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { SipnnerService } from 'src/app/_services/SpinnerService';
import { DateTimeServiceService } from 'src/app/_services/date-time-service.service';

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.css']
})
export class SpinnerComponent {

 
  spinnerActive: boolean = true;

  constructor(
    public spinnerHandler: SipnnerService
  ) {
    this.spinnerHandler.showSpinner.subscribe(this.showSpinner.bind(this));
  }

  showSpinner = (state: boolean): void => {
    this.spinnerActive = state;
  };

}
