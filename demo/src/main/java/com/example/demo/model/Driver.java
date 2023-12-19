package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Driver {

	private String driverId;
	private String code;
	private String url;
	private String permanentNumber;
	private String givenName;
	@JsonProperty("familyName")
	private String familyName;
	private Date dateOfBirth;
	private String nationality;
	private String flagCode;
	private String constructorId;
	private String winsSeason;
	
	
	
	public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPermanentNumber() {
		return permanentNumber;
	}
	public void setPermanentNumber(String permanentNumber) {
		this.permanentNumber = permanentNumber;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getFlagCode() {
		return flagCode;
	}
	public void setFlagCode(String flagCode) {
		this.flagCode = flagCode;
	}
	public String getConstructorId() {
		return constructorId;
	}
	public void setConstructorId(String constructor) {
		this.constructorId = constructor;
	}
	public String getWinsSeason() {
		return winsSeason;
	}
	public void setWinsSeason(String winsSeason) {
		this.winsSeason = winsSeason;
	}
	
	
	
	
	
}
