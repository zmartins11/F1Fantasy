package com.example.f1.auth_service.service;

import com.example.f1.auth_service.model.User;
import com.example.f1.auth_service.repository.AuthenticationRepository;
import com.example.f1.auth_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService {
	
	private final AuthenticationRepository authRepo;

	private final UserRepository userRepository;

	public AuthenticationService(AuthenticationRepository authRepo, UserRepository userRepository) {
		this.authRepo = authRepo;
		this.userRepository = userRepository;
	}


	public User saveUser(User user) {
		return authRepo.save(user);	
	}
	
	public User fetchUserByEmailId(String email) {
		return authRepo.findByEmailId(email);
	}
	
	public User fetchUserByEmailAndPassword(String email, String pass) {
		return authRepo.findByEmailIdAndPassword(email, pass);
	}

	public Optional<User> findByUsername(String username) {
		return userRepository.findByUserName(username);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}
}
