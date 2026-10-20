export interface ScoringHistory {
  predictionId: string;
  season: number;
  round: number;
  points: number;
}

export interface ScoringHistoryPage {
  content: ScoringHistory[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}

export interface ScoringHistoryView extends ScoringHistory {
  raceName: string;
   country: string;
}
