package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.enums.Formula1DriverEnum;
import com.example.demo.model.*;
import com.example.demo.model.fantasy.RaceResult;
import com.example.utils.RaceResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ErgastService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final PredictService predictService;

	private final static String SEASON_2023 = "2023";
	private final static String SEASON_2022 = "2022";


	private final RaceResultMapper resultMapper = new RaceResultMapper();
	@Autowired
	public ErgastService(PredictService predictService) {
		this.predictService = predictService;
	}

	public List<Race> getRaces(String season) throws JsonProcessingException {
		String url = "http://ergast.com/api/f1/" + season + "/races.json";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		RaceResponse racesResponse = mapper.readValue(response.getBody(), RaceResponse.class);
		return racesResponse.getMrData().getRaceTable().getRace();

	}
	
	public List<Results> getRaceResult(String season, String round) throws JsonMappingException, JsonProcessingException {
		
		List<Results> resultsRace = null;
		String urlRaceResult = "http://ergast.com/api/f1/" + season + "/" + round + "/results.json?limit=3";
		
		ResponseEntity<String> response = restTemplate.getForEntity(urlRaceResult, String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		RaceResultsResponse resultsResponse = objectMapper.readValue(response.getBody(),RaceResultsResponse.class);
	
		resultsRace = resultsResponse.getMrData().getRaceTable().getRaces().get(0).getResults();

		RaceResult raceResult = resultMapper.map(resultsRace,round, season);

		predictService.saveRace(raceResult);
		
		return resultsRace;
		
	}
	
	public List<Driver> rawData(String season, String round) throws JsonProcessingException {
		
		List<Driver> driversInSeason = null;
		
		String urlDriversByYear = "http://ergast.com/api/f1/" + season + "/drivers.json";
		
		ResponseEntity<String> response = restTemplate.getForEntity(urlDriversByYear, String.class);
		
		
		ObjectMapper mapper = new ObjectMapper();
		DriverResponse driverResponse = mapper.readValue(response.getBody(), DriverResponse.class);
		
		driversInSeason = driverResponse.getMrData().getDriverTable().getDrivers();
		
		getFlagCode(driversInSeason);
		
		//getting the constructor 
//		http://ergast.com/api/f1/2010/drivers/alonso/constructors
		for (Driver driver : driversInSeason) {
			
			String constructorReturned = null;
			String urlConstructor = "http://ergast.com/api/f1/" + season + "/drivers/" + driver.getDriverId() + "/constructors.json";
			
			ResponseEntity<String> responseConstructor = restTemplate.getForEntity(urlConstructor, String.class);
			
			ObjectMapper mapperC = new ObjectMapper();
			ConstructorResponse constructorResponse = mapperC.readValue(responseConstructor.getBody(), ConstructorResponse.class);

			constructorReturned = constructorResponse.getMrData().getConstructorTable().getConstructors().get(0).getName();
			
			if(constructorReturned != null) {
				driver.setConstructorId(constructorReturned);
			}
			///GET WINS 
//			http://ergast.com/api/f1/2010/drivers/alonso/results/1
			String winsUrl  = "http://ergast.com/api/f1/" + season + "/drivers/" + driver.getDriverId() + "/results/1.json";
			
			ResponseEntity<String> responseWins = restTemplate.getForEntity(winsUrl,String.class);
			ObjectMapper mapperW = new ObjectMapper();
			mapperW.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			WinsResponse data = mapperW.readValue(responseWins.getBody(),WinsResponse.class);
			String winsSeason = String.valueOf(data.getMrData().getTotal());
			
			if (winsSeason != null ) {
				driver.setWinsSeason(winsSeason);
			}
		}
		return driversInSeason;
	}
	
	private void getFlagCode(List<Driver> driversInSeason) {
		
		Map<String, String> countryCodeMap = new HashMap<>();
		countryCodeMap.put("german", "de");
		countryCodeMap.put("british", "gb");
		countryCodeMap.put("italian", "it");
		countryCodeMap.put("french ", "fr");
		countryCodeMap.put("spanish", "es");
		countryCodeMap.put("thai", "th");
		countryCodeMap.put("finnish", "fi");
		countryCodeMap.put("dutch", "nl");
		countryCodeMap.put("canadian", "ca");
		countryCodeMap.put("monegasque", "mc");
		countryCodeMap.put("danish", "dk");
		countryCodeMap.put("french", "fr");
		countryCodeMap.put("mexican", "mx");
		countryCodeMap.put("australian", "au");
		countryCodeMap.put("japanese", "jp");
		countryCodeMap.put("chinese", "cn");
		
		
		
		for(Driver driver : driversInSeason) {
			String flagCode = countryCodeMap.get(driver.getNationality().toLowerCase());
			if(flagCode != null) {
				driver.setFlagCode(flagCode);
			}
		}
		
	}

	public ArrayList<String> getNameRaces(String season) throws JsonMappingException, JsonProcessingException {
		ArrayList<String> names = new ArrayList<>();
		
		String url = "http://ergast.com/api/f1/" + season + "/races.json";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		RaceResponse racesResponse = mapper.readValue(response.getBody(), RaceResponse.class);
		List<Race> races = racesResponse.getMrData().getRaceTable().getRace();
		for(Race r :races) {
			names.add(r.getRaceName());
		}
		
		return names;
	}

	public int testApiGetResult(String position) throws JsonProcessingException {
		///GET WINS
//			http://ergast.com/api/f1/2010/drivers/alonso/results/1
		int racesCurrentSeason = predictService.getSeasonRaces(SEASON_2023);
		int numberResult = 0;
		String winsUrl  = "http://ergast.com/api/f1/" + SEASON_2023 + "/drivers/" + Formula1DriverEnum.PEREZ.getName().toLowerCase() + "/results/1.json";

		ResponseEntity<String> responseWins = restTemplate.getForEntity(winsUrl,String.class);
		ObjectMapper mapperW = new ObjectMapper();
		mapperW.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		WinsResponse data = mapperW.readValue(responseWins.getBody(),WinsResponse.class);
		numberResult = data.getMrData().getTotal();

		if (racesCurrentSeason < 20) {
			String winsUrlLastSeason  = "http://ergast.com/api/f1/" + SEASON_2022 + "/drivers/" + Formula1DriverEnum.PEREZ.getName().toLowerCase() + "/results/1.json";

			ResponseEntity<String> responseWinsLastSeason = restTemplate.getForEntity(winsUrlLastSeason,String.class);
			ObjectMapper mapperWLastSeason = new ObjectMapper();
			mapperWLastSeason.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			WinsResponse dataLastSeason = mapperWLastSeason.readValue(responseWinsLastSeason.getBody(),WinsResponse.class);
			int resultLastSeason = dataLastSeason.getMrData().getTotal();
			numberResult += resultLastSeason;
		}
		return numberResult;
	}

}
