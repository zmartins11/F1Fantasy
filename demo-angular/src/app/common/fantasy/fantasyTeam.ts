import { Driverf } from './driverf';
import { Team } from './teamf';
import { User } from '../user';

export class FantasyTeam {
  id!: number;
  user!: User;
  team!: Team;
  driver1!: Driverf;
  driver2!: Driverf;
  driver3!: Driverf;
  budget !: number;

getTotalCost(): number {
  return this.driver1.price + this.driver2.price + this.driver3.price;
}


}


