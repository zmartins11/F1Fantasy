package com.example.f1.auth_service.controller;


import com.example.f1.auth_service.config.JWTGenerator;
import com.example.f1.auth_service.dto.AuthReponseDto;
import com.example.f1.auth_service.dto.LoginDto;
import com.example.f1.auth_service.dto.RegisterDto;
import com.example.f1.auth_service.model.User;
import com.example.f1.auth_service.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JWTGenerator jwtGenerator;

	public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository,
									PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtGenerator = jwtGenerator;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthReponseDto> login(@RequestBody LoginDto loginDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		Optional<User> user = userRepository.findByUserName(loginDto.getUsername());

		AuthReponseDto authResponse = new AuthReponseDto();
		String token = jwtGenerator.generateToken(authentication, user.get().getId());
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		if (!authorities.isEmpty()) {
			authResponse.setRole(authorities.iterator().next().getAuthority());
		}
		authResponse.setAccessToken(token);
		authResponse.setUsername(loginDto.getUsername());
		authResponse.setUserId(userRepository.findByUserName(loginDto.getUsername())
				.orElseThrow(() -> new IllegalStateException("Authenticated user not found"))
				.getId());

		return ResponseEntity.ok(authResponse);
	}

	@PostMapping("/register")
	public ResponseEntity<String> register (@RequestBody RegisterDto registerDto) {
		if (userRepository.existsByUserName(registerDto.getUsername())) {
			return ResponseEntity.badRequest().body("UserName is taken!");
		}

		User user = new User();
		user.setUserName(registerDto.getUsername());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		user.setEmailId(registerDto.getEmail());

		userRepository.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("User registered success!!");

	}

	@GetMapping("/test")
	public ResponseEntity<String> testApi() {
		return ResponseEntity.ok("Testing home Page");
	}

}
