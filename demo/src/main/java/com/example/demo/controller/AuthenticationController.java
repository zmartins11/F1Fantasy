package com.example.demo.controller;

import com.example.demo.dto.AuthReponseDto;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.model.fantasy.Roles;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.fantasy.User;
import com.example.demo.service.AuthenticationService;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthenticationController {

	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private JWTGenerator jwtGenerator;

	@Autowired
	public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository,
									RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtGenerator = jwtGenerator;
	}

	@PostMapping("login")
	public ResponseEntity<AuthReponseDto> login(@RequestBody LoginDto loginDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginDto.getUserName(),
						loginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		AuthReponseDto authReponseDto = new AuthReponseDto();
		String token = jwtGenerator.generateToken(authentication);
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		if (iterator.hasNext()) {
			GrantedAuthority authority = iterator.next();
			authReponseDto.setAuthorities(authority.getAuthority());
		}
		authReponseDto.setAccessToken(token);

		return new ResponseEntity<>(authReponseDto, HttpStatus.OK);
	}

	@PostMapping("register")
	public ResponseEntity<String> register (@RequestBody RegisterDto registerDto) {
		if (userRepository.existsByUserName(registerDto.getUserName())) {
			return new ResponseEntity<>("UserName is taken!" , HttpStatus.BAD_REQUEST);
		}

		User user = new User();
		user.setUserName(registerDto.getUserName());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		Roles roles = roleRepository.findByName("USER").get();
		user.setRoles(Collections.singletonList(roles));

		userRepository.save(user);
		return new ResponseEntity<>("User registered success!!", HttpStatus.OK);
	}

}
