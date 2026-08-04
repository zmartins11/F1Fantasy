export interface RaceResults {
  round: number;
  circuit: string;
  season: string;
  first: string;
  second: string;
  third: string;
  fastestLap: string;
  status: string;
  raceFinished: Boolean;
  predictionLocked : Boolean;
}