export interface RaceResults {
    number: string;
  position: string;
  positionText: string;
  points: string;
  grid: string;
  laps: string;
  status: string;
  time: any;
  Driver: {
    driverId: string;
    code: string;
    url: string;
    permanentNumber: string;
    givenName: string;
    familyName: string;
    dateOfBirth: string;
    nationality: string;
    flagCode: string;
    constructorId: any;
    winsSeason: any;
  };
  Constructor: {
    constructorId: string;
    url: string;
    name: string;
    nationality: string;
  };
}