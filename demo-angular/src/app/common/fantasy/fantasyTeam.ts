import { Driverf } from './driverf';
import { Team } from './teamf';
import { User } from '../user';

export interface FantasyTeam {
  id: number;
  user: User;
  team: Team;
  driver1: Driverf;
  driver2: Driverf;
  driver3: Driverf;
}