import { TotalPointsResponse } from "./TotalPointsResponse";

export interface Standings {
    drivers: TotalPointsResponse[];
    constructors: TotalPointsResponse[];
  }