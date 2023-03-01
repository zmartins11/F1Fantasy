package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.fantasy.User;
import com.example.demo.repository.AuthenticationRepository;

@Service
public class AuthenticationService {
	
	@Autowired
	private AuthenticationRepository authRepo;

	
	public User saveUser(User user) {
		return authRepo.save(user);	
	}
	
	public User fetchUserByEmailId(String email) {
		return authRepo.findByEmailId(email);
	}
	
	public User fetchUserByEmailAndPassword(String email, String pass) {
		return authRepo.findByEmailIdAndPassword(email, pass);
	}
}
