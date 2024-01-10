export interface Race {
   
    season: string;
    round: string;
    url: string;
    raceName: string;
    results: any[];
    date: string;
    time: string;
    Circuit: {
      circuitId: string;
      url: string;
      circuitName: string;
      Location: {
        lat: string;
        locality: string;
        country: string;
        long: string;
      };
    };
    FirstPractice: {
      date: string;
      time: string;
    };
    SecondPractice: {
        date: string;
        time: string;
    };
    ThirdPractice: {
        date: string;
        time: string;
    };
    Qualifying: {
      date: string;
      time: string;
  };
  Sprint: {
    date: string;
    time: string;
};
}