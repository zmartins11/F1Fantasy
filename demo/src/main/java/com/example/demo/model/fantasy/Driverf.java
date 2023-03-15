package com.example.demo.model.fantasy;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;


/**
 * The persistent class for the driverf database table.
 * 
 */
@Entity
@NamedQuery(name="Driverf.findAll", query="SELECT d FROM Driverf d")
public class Driverf implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private String name;

	@Column(name="photo_url")
	private String photoUrl;
	
	private int price;

	public Driverf() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhotoUrl() {
		return this.photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}