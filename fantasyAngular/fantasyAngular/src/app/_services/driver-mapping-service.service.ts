import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DriverMappingService {

  constructor() { }

  private driverMap: { [key: number]: string } = {
    44: 'Hamilton',
    77: 'Valtteri Bottas',
    1: 'Verstappen',
    11: 'Perez',
    14: 'Alonso',
    4: 'Norris',
    16: 'Leclerc',
    55: 'Sainz',
    63: 'Russel',
    81: 'Piastri',
    10: 'Gasly',
    18: 'Stroll',
    31: 'Ocon',
    23: 'Albon',
    27: 'Hulkenberg',
    22: 'Tsunoda',
    3: 'Ricciardo',
    24: 'Zhou',
    20 : 'Magnussen',
    2: 'Sargeant'

    // Add more mappings as needed
  };

  getDriverName(driverNumber: number): string {
    return this.driverMap[driverNumber] || 'Unknown Driver';
  }
}
