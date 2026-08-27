export interface Prediction {
    userId : number;
    first : string;
    second : string;
    third : string;
    round : number;
    fastestLap: string;
    predictedPodium: Boolean;
    predictedFastestLap: Boolean;
}