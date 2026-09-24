export interface NextRaceInfo {
    race: NextRaceData;
    prediction: UserPredictionData;
}

export interface NextRaceData {
    nameRace : string;
    time : string; 
    round : string;
    country: string;
    predictionLocked: boolean;
    city: string;
    raceDate: string;
    raceTime: string;
}

export interface UserPredictionData {
    userHavePrediction : Boolean;
    first : string;
    second : string;
    third : string;
    fastestLap: string;
    predictedPodium: Boolean;
    predictedFastestLap : Boolean;
}