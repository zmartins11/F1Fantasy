export class Formula1Driver {
  selection?: number;
  constructor(public name: string, public number: number) {}

}
export const Formula1Drivers: Formula1Driver[] = [
  new Formula1Driver('Verstappen', 1),
  new Formula1Driver('Perez', 11),
  new Formula1Driver('Hamilton', 44),
  new Formula1Driver('Alonso', 14),
  new Formula1Driver('Norris', 4),
  new Formula1Driver('Leclerc', 16),
  new Formula1Driver('Sainz', 55),
  new Formula1Driver('Russell', 63),
  new Formula1Driver('Piastri',81),
  new Formula1Driver('Gasly',10),
  new Formula1Driver('Stroll',18),
  new Formula1Driver('Ocon',31),
  new Formula1Driver('Albon',23),
  new Formula1Driver('Bottas',77),
  new Formula1Driver('Hulkenberg',27),
  new Formula1Driver('Tsunoda',22),
  new Formula1Driver('Ricciardo',3),
  new Formula1Driver('Magnussen',20),
  new Formula1Driver('Sargeant',2),
  new Formula1Driver('Zhou', 24)
];