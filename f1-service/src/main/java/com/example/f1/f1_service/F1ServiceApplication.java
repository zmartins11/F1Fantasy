package com.example.f1.f1_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class F1ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(F1ServiceApplication.class, args);
	}

}
