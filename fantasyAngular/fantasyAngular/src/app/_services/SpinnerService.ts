import { Injectable } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SipnnerService {

  public numberOfRequests: number = 0;
  public showSpinner: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  handleRequest(state: string = 'minus'): void {

    if (state === 'plus') {
      this.numberOfRequests++;
    } else {
      this.numberOfRequests--;
    }

    if (this.numberOfRequests < 0) {
      this.numberOfRequests = 0;
    }

    console.log("Spinner:", state, "contador =", this.numberOfRequests);

    this.showSpinner.next(this.numberOfRequests > 0);
  }
  

}
