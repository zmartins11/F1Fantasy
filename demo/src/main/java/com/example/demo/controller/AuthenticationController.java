package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.AuthenticationService;

@RestController
@CrossOrigin("*")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authService;
	
	
	@PostMapping("/registerUser")
	public User registerUser(@RequestBody User user) throws Exception {
		
		String tempEmail = user.getEmailId();
		if((tempEmail != null) && (!tempEmail.equals(""))) {
			User userRepetead = authService.fetchUserByEmailId(tempEmail);
			if (userRepetead != null) {
				throw new Exception("user with " + tempEmail + " already exists!");
			}
			
		}
		
		User userObj = null;
		userObj = authService.saveUser(user);
		return userObj;
	}
	
	@PostMapping("/login")
	public User loginUser(@RequestBody User user) throws Exception {
		String tempEmail = user.getEmailId();
		String tempPassword = user.getPassword();
		User userObj= null;
		if((tempEmail != null) && (tempPassword != null)) {
			userObj = authService.fetchUserByEmailAndPassword(tempEmail, tempPassword);
		}
		if(userObj == null) {
			throw new Exception("bad Credentials");
		}
		return userObj;
	}
}
