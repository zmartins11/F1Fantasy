import { Injectable } from '@angular/core';
import { Formula1Drivers } from '../model/Formula1Drivers';

@Injectable({
  providedIn: 'root'
})
export class F1DriversService {

  constructor() { }

  static getDriverNameByNumber(driverNumber: string): string | undefined {
    const number = parseInt(driverNumber);
    const selectedDriver = Formula1Drivers.find(driver => driver.number === number);
    return selectedDriver ? selectedDriver.name : undefined;
  }
}
