package com.example.demo.service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import com.example.demo.dto.*;
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

import javax.xml.transform.Result;

@Service
public class ErgastService {

	private final RestTemplate restTemplate = new RestTemplate();

	private final static String SEASON_2023 = "2023";
	private final static String SEASON_2022 = "2022";


	private final RaceResultMapper resultMapper = new RaceResultMapper();


	public List<Race> getRaces(String season) throws JsonProcessingException {
		String url = "https://api.jolpi.ca/ergast/f1/" + season + "/races.json";

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		RaceResponse racesResponse = mapper.readValue(response.getBody(), RaceResponse.class);
		return racesResponse.getMrData().getRaceTable().getRace();
	}

	public RaceResultDto getRaceResult(String season, String round)
			throws JsonProcessingException {

		String urlRaceResult =
				"https://api.jolpi.ca/ergast/f1/" + season + "/" + round + "/results.json?limit=3";
		ResponseEntity<String> response =
				restTemplate.getForEntity(urlRaceResult, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		RaceResultsResponse resultsResponse =
				objectMapper.readValue(response.getBody(), RaceResultsResponse.class);

		RaceResultDto dto = new RaceResultDto();
		List<Race> races = resultsResponse.getMrData().getRaceTable().getRaces();
		if (races == null || races.isEmpty()) {
			dto.setSeason(season);
			dto.setRound(Integer.parseInt(round));
			dto.setRaceFinished(false);
			return dto;
		}
		List<Results> resultsRace =
				resultsResponse.getMrData()
						.getRaceTable()
						.getRaces()
						.get(0)
						.getResults();

		String fastestLapUrl =
				"https://api.jolpi.ca/ergast/f1/" + season + "/" + round + "/fastest/1/results.json";

		ResponseEntity<String> responseFastest =
				restTemplate.getForEntity(fastestLapUrl, String.class);

		RaceResultsResponse fastestResponse =
				objectMapper.readValue(responseFastest.getBody(), RaceResultsResponse.class);

		Results fastestLap =
				fastestResponse.getMrData()
						.getRaceTable()
						.getRaces()
						.get(0)
						.getResults()
						.get(0);

		dto = resultMapper.map(resultsRace, fastestLap, round, season);
		dto.setRaceFinished(true);

		return dto;
	}

	public List<Driver> rawData(String season) throws JsonProcessingException {

		List<Driver> driversInSeason = null;

		String urlDriversByYear = "https://api.jolpi.ca/ergast/f1/" + season + "/drivers.json";

		ResponseEntity<String> response = restTemplate.getForEntity(urlDriversByYear, String.class);


		ObjectMapper mapper = new ObjectMapper();
		DriverResponse driverResponse = mapper.readValue(response.getBody(), DriverResponse.class);

		driversInSeason = driverResponse.getMrData().getDriverTable().getDrivers();

		//remove drivers without nationality
		driversInSeason.removeIf(driver ->
				driver.getNationality() == null || driver.getNationality().isBlank());

		getFlagCode(driversInSeason);

		//getting the constructor
//		http://ergast.com/api/f1/2010/drivers/alonso/constructors
		/*for (Driver driver : driversInSeason) {

			String constructorReturned = null;
			String urlConstructor = "https://api.jolpi.ca/ergast/f1/" + season + "/drivers/" + driver.getDriverId() + "/constructors.json";

			ResponseEntity<String> responseConstructor = restTemplate.getForEntity(urlConstructor, String.class);

			ObjectMapper mapperC = new ObjectMapper();
			ConstructorResponse constructorResponse = mapperC.readValue(responseConstructor.getBody(), ConstructorResponse.class);

			constructorReturned = constructorResponse.getMrData().getConstructorTable().getConstructors().get(0).getName();

			if (constructorReturned != null) {
				driver.setConstructorId(constructorReturned);
			}
			///GET WINS
//			http://ergast.com/api/f1/2010/drivers/alonso/results/1
			String winsUrl = "https://api.jolpi.ca/ergast/f1/" + season + "/drivers/" + driver.getDriverId() + "/results/1.json";

			ResponseEntity<String> responseWins = restTemplate.getForEntity(winsUrl, String.class);
			ObjectMapper mapperW = new ObjectMapper();
			mapperW.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			WinsResponse data = mapperW.readValue(responseWins.getBody(), WinsResponse.class);
			String winsSeason = String.valueOf(data.getMrData().getTotal());

			driver.setWinsSeason(winsSeason);
		} */
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


		for (Driver driver : driversInSeason) {
			String flagCode = countryCodeMap.get(driver.getNationality().toLowerCase());
			if (flagCode != null) {
				driver.setFlagCode(flagCode);
			}
		}
	}

	public ArrayList<String> getNameRaces(String season) throws JsonMappingException, JsonProcessingException {
		ArrayList<String> names = new ArrayList<>();

		String url = "https://api.jolpi.ca/ergast/f1/" + season + "/races.json";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		RaceResponse racesResponse = mapper.readValue(response.getBody(), RaceResponse.class);
		List<Race> races = racesResponse.getMrData().getRaceTable().getRace();
		for (Race r : races) {
			names.add(r.getRaceName());
		}

		return names;
	}


	public List<RaceInfo> getAllRaces(String season) throws JsonMappingException, JsonProcessingException {
		String url = "https://api.jolpi.ca/ergast/f1/" + season + "/races.json";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		RaceResponse racesResponse = mapper.readValue(response.getBody(), RaceResponse.class);

		return racesResponse.getMrData()
				.getRaceTable()
				.getRaces()
				.stream()
				.map(this::toRaceInfo)
				.toList();
	}

	private RaceInfo toRaceInfo(Race race) {
		RaceInfo raceInfo = new RaceInfo();

		raceInfo.setCountry(
				"United States".equals(race.getCircuit().getLocation().getCountry())
						? "USA"
						: race.getCircuit().getLocation().getCountry());

		raceInfo.setRaceName(race.getRaceName());
		raceInfo.setRound(race.getRound());

		return raceInfo;
	}

	public HashMap<Integer, Integer> testApiGetResult(String position, Integer racesCurrentSeason, String driver) throws JsonProcessingException {
		HashMap<Integer, Integer> resultSeason = new HashMap<>();

		int races2022Season = 22;
		racesCurrentSeason = 19;

		int numberResult = 0;
		String winsUrl = "https://api.jolpi.ca/ergast/f1/" + SEASON_2023 + "/drivers/" + driver.toLowerCase() + "/results/" + position + ".json";

		ResponseEntity<String> responseWins = restTemplate.getForEntity(winsUrl, String.class);
		ObjectMapper mapperW = new ObjectMapper();
		mapperW.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		WinsResponse data = mapperW.readValue(responseWins.getBody(), WinsResponse.class);
		numberResult = data.getMrData().getTotal();

		resultSeason.put(numberResult, racesCurrentSeason);

		if (racesCurrentSeason < 20) {
			String winsUrlLastSeason = "https://api.jolpi.ca/ergast/f1/" + SEASON_2022 + "/drivers/" + driver.toLowerCase() + "/results/" + position + ".json";

			ResponseEntity<String> responseWinsLastSeason = restTemplate.getForEntity(winsUrlLastSeason, String.class);
			ObjectMapper mapperWLastSeason = new ObjectMapper();
			mapperWLastSeason.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			WinsResponse dataLastSeason = mapperWLastSeason.readValue(responseWinsLastSeason.getBody(), WinsResponse.class);
			int resultLastSeason = dataLastSeason.getMrData().getTotal();
			resultSeason.remove(numberResult);
			numberResult += resultLastSeason;
			resultSeason.put(numberResult, racesCurrentSeason + races2022Season);
		}
		return resultSeason;
	}

	public NextRaceInfoDto getScheduleRace() throws JsonProcessingException {

		ResponseEntity<String> response;

		String url = "https://api.jolpi.ca/ergast/f1/current/races/";

		try {
			response = restTemplate.getForEntity(url, String.class);
		} catch (HttpServerErrorException e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatusCode.valueOf(500));
		}

		ObjectMapper mapper = new ObjectMapper();
		RaceResponse racesResponse = mapper.readValue(response.getBody(), RaceResponse.class);

		LocalDateTime now = LocalDateTime.now();

		Race nextRace = racesResponse.getMrData()
				.getRaceTable()
				.getRaces()
				.stream()
				.filter(race -> {

					LocalTime raceTime = race.getTime() != null
							? race.getTime()
							: LocalTime.MIDNIGHT;

					LocalDateTime raceDateTime = LocalDateTime.of(
							race.getDate(),
							raceTime);

					return raceDateTime.isAfter(now);

				})
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No next race found"));

		NextRaceInfoDto dto = new NextRaceInfoDto();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

		LocalTime raceTime = nextRace.getTime() != null
				? nextRace.getTime()
				: LocalTime.MIDNIGHT;

		LocalDateTime raceDateTime = LocalDateTime.of(
				nextRace.getDate(),
				raceTime);

		dto.setTime(raceDateTime.format(formatter));
		dto.setNameRace(nextRace.getRaceName());
		dto.setRound(nextRace.getRound());
		dto.setCountry(nextRace.getCircuit().getLocation().getCountry());
		dto.setCity(nextRace.getCircuit().getLocation().getLocality());
		dto.setRaceDate(nextRace.getDate());
		dto.setRaceTime(nextRace.getTime());

		// Bloquear previsões 24 horas antes da qualificação
		if (nextRace.getQualifying() != null) {

			LocalTime qualiTime = nextRace.getQualifying().getTime() != null
					? nextRace.getQualifying().getTime()
					: LocalTime.MIDNIGHT;

			LocalDateTime qualiDateTime = LocalDateTime.of(
					nextRace.getQualifying().getDate(),
					qualiTime);

			dto.setPredictionLocked(
					now.isAfter(qualiDateTime.minusHours(24)));

		} else {

			dto.setPredictionLocked(false);

		}

		return dto;
	}

	public Race getNextRace() throws JsonProcessingException {

		ResponseEntity<String> response;

		String url = "https://api.jolpi.ca/ergast/f1/current/races/";

		try {
			response = restTemplate.getForEntity(url, String.class);
		} catch (HttpServerErrorException e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatusCode.valueOf(500));
		}

		ObjectMapper mapper = new ObjectMapper();
		RaceResponse racesResponse = mapper.readValue(response.getBody(), RaceResponse.class);

		LocalDateTime now = LocalDateTime.now();

		Race nextRace = racesResponse.getMrData()
				.getRaceTable()
				.getRaces()
				.stream()
				.filter(race -> {

					LocalTime raceTime = race.getTime() != null
							? race.getTime()
							: LocalTime.MIDNIGHT;

					LocalDateTime raceDateTime = LocalDateTime.of(
						race.getDate(),
						raceTime);

					return raceDateTime.isAfter(now);

				})
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No next race found"));

		return nextRace;
	}

	public StandingsDto getStandings() throws JsonProcessingException {
		StandingsDto standingsDto = new StandingsDto();
		RestTemplate restTemplate = new RestTemplate();

		List<TotalPointsDto> resultDrivers  = new ArrayList<>();
		List<TotalPointsDto> resultConstructors  = new ArrayList<>();


		String urlDrivers = "https://api.jolpi.ca/ergast/f1/current/driverstandings.json";
		String urlConstructor = "https://api.jolpi.ca/ergast/f1/current/constructorStandings.json";

		ResponseEntity<String> responseDriver = restTemplate.getForEntity(urlDrivers, String.class);
		ResponseEntity<String> responseConstructor = restTemplate.getForEntity(urlConstructor, String.class);

		ObjectMapper mapper = new ObjectMapper();
		RaceResponse racesResponseDrivers = mapper.readValue(responseDriver.getBody(), RaceResponse.class);
		RaceResponse racesResponseConstructor = mapper.readValue(responseConstructor.getBody(), RaceResponse.class);

		if (racesResponseDrivers != null) {
			List<DriverStanding> drivers = racesResponseDrivers.getMrData().getStandingsTable().getStandingsLists().get(0).getDriverStandings();
			for(DriverStanding driverTemp : drivers) {
				TotalPointsDto tmp = new TotalPointsDto();
				tmp.setUsername(driverTemp.getDriver().getFamilyName());
				tmp.setPoints(driverTemp.getPoints());
				resultDrivers.add(tmp);
			}

		}

		if (racesResponseConstructor != null) {
			List<ConstructorStandings> constructors = racesResponseConstructor.getMrData().getStandingsTable().getStandingsLists().get(0).getConstructorStandings();
			for (ConstructorStandings constructorTemp : constructors) {
				TotalPointsDto tmpC = new TotalPointsDto();
				tmpC.setUsername(constructorTemp.getConstructor().getName());
				tmpC.setPoints(constructorTemp.getPoints());
				resultConstructors.add(tmpC);
			}

		}

		resultDrivers.sort((o1, o2) -> Integer.compare(Integer.parseInt(o2.getPoints()), Integer.parseInt(o1.getPoints())));
		for (int i = 0; i < resultDrivers.size(); i++) {
			TotalPointsDto tmp = resultDrivers.get(i);
			tmp.setPosition(String.valueOf(i + 1));
		}

		resultConstructors.sort((o1, o2) -> Integer.compare(Integer.parseInt(o2.getPoints()), Integer.parseInt(o1.getPoints())));
		for (int i = 0; i < resultConstructors.size(); i++) {
			TotalPointsDto tmp = resultConstructors.get(i);
			tmp.setPosition(String.valueOf(i + 1));
		}

		standingsDto.setDrivers(resultDrivers);
		standingsDto.setConstructors(resultConstructors);

		return standingsDto;
	}

	public Race getRaceResults(String season, String round) throws JsonProcessingException {

		String url = "https://api.jolpi.ca/ergast/f1/" + season + "/" + round + "/results.json";

		ResponseEntity<String> response;

		try {
			response = restTemplate.getForEntity(url, String.class);
		} catch (HttpServerErrorException e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatusCode.valueOf(500));
		}

		ObjectMapper mapper = new ObjectMapper();

		RaceResponse raceResponse = mapper.readValue(response.getBody(), RaceResponse.class);

		return raceResponse.getMrData()
				.getRaceTable()
				.getRaces()
				.stream()
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Race not found"));
	}
}
