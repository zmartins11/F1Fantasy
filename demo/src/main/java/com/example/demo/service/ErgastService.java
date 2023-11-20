package com.example.demo.service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.example.demo.dto.DateTimeResponseDto;
import com.example.demo.dto.NextRaceInfoDto;
import com.example.demo.model.*;
import com.example.demo.model.fantasy.RaceResult;
import com.example.utils.RaceResultMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ErgastService {

	private final RestTemplate restTemplate = new RestTemplate();

	private final static String SEASON_2023 = "2023";
	private final static String SEASON_2022 = "2022";


	private final RaceResultMapper resultMapper = new RaceResultMapper();


	public List<Race> getRaces(String season) throws JsonProcessingException {
		String url = "http://ergast.com/api/f1/" + season + "/races.json";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		RaceResponse racesResponse = mapper.readValue(response.getBody(), RaceResponse.class);
		return racesResponse.getMrData().getRaceTable().getRace();

	}
	
	public RaceResult getRaceResult(String season, String round) throws JsonMappingException, JsonProcessingException {
		List<Results> resultsRace = null;
		String urlRaceResult = "http://ergast.com/api/f1/" + season + "/" + round + "/results.json?limit=3";
		
		ResponseEntity<String> response = restTemplate.getForEntity(urlRaceResult, String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		RaceResultsResponse resultsResponse = objectMapper.readValue(response.getBody(),RaceResultsResponse.class);
	
		resultsRace = resultsResponse.getMrData().getRaceTable().getRaces().get(0).getResults();
		RaceResult raceResult = resultMapper.map(resultsRace,round, season);

		return raceResult;
		
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

	public HashMap<Integer,Integer> testApiGetResult(String position, Integer racesCurrentSeason, String driver) throws JsonProcessingException {
		HashMap<Integer,Integer> resultSeason = new HashMap<>();

		int races2022Season = 22;
		racesCurrentSeason = 19;

		int numberResult = 0;
		String winsUrl  = "http://ergast.com/api/f1/" + SEASON_2023 + "/drivers/" + driver.toLowerCase() + "/results/"+ position + ".json";

		ResponseEntity<String> responseWins = restTemplate.getForEntity(winsUrl,String.class);
		ObjectMapper mapperW = new ObjectMapper();
		mapperW.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		WinsResponse data = mapperW.readValue(responseWins.getBody(),WinsResponse.class);
		numberResult = data.getMrData().getTotal();

		resultSeason.put(numberResult, racesCurrentSeason);

		if (racesCurrentSeason < 20) {
			String winsUrlLastSeason  = "http://ergast.com/api/f1/" + SEASON_2022 + "/drivers/" + driver.toLowerCase() + "/results/" + position + ".json";

			ResponseEntity<String> responseWinsLastSeason = restTemplate.getForEntity(winsUrlLastSeason,String.class);
			ObjectMapper mapperWLastSeason = new ObjectMapper();
			mapperWLastSeason.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			WinsResponse dataLastSeason = mapperWLastSeason.readValue(responseWinsLastSeason.getBody(),WinsResponse.class);
			int resultLastSeason = dataLastSeason.getMrData().getTotal();
			resultSeason.remove(numberResult);
			numberResult += resultLastSeason;
			resultSeason.put(numberResult, racesCurrentSeason + races2022Season);
		}
		return resultSeason;
	}

    public NextRaceInfoDto getScheduleRace(Optional<RaceResult> nextRace) throws JsonProcessingException {

		NextRaceInfoDto nextRaceInfo = new NextRaceInfoDto();
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

		ResponseEntity<String> response = null;

		//get next race info
		String round = nextRace.get().getRound();
		String url = "http://ergast.com/api/f1/2023/" + round + ".json";
		try {
			response = restTemplate.getForEntity(url, String.class);
		} catch (HttpServerErrorException e){
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatusCode.valueOf(500));
		}
		ObjectMapper mapper = new ObjectMapper();
		RaceResponse racesResponse = mapper.readValue(response.getBody(), RaceResponse.class);
		LocalDate qualiDate = racesResponse.getMrData().getRaceTable().getRaces().get(0).getQualifying().getDate();
		LocalTime qualiTime = racesResponse.getMrData().getRaceTable().getRaces().get(0).getQualifying().getTime();

		//convert time to gmt
		LocalDateTime raceDateTime = LocalDateTime.of(2023, 1, 1, qualiTime.getHour(), qualiTime.getMinute(), qualiTime.getSecond());
		ZonedDateTime gmtQualiDateTime = ZonedDateTime.of(raceDateTime, ZoneId.of("GMT"));
		LocalTime gmtQualiTime = gmtQualiDateTime.toLocalTime();


 		LocalDateTime localDateTimeQuali = LocalDateTime.of(qualiDate, gmtQualiTime);

		//check if have to lock the predicion

		if (currentDateTime.isAfter(localDateTimeQuali)) {
			nextRaceInfo.setPredictionLocked(Boolean.TRUE);
		} else {
			nextRaceInfo.setPredictionLocked(Boolean.FALSE);
		}

		nextRaceInfo.setTime(localDateTimeQuali.format(dateTimeFormatter));
		nextRaceInfo.setNameRace(racesResponse.getMrData().getRaceTable().getRaces().get(0).getRaceName());
		nextRaceInfo.setRound(racesResponse.getMrData().getRaceTable().getRaces().get(0).getRound());
		return nextRaceInfo;
    }
}
