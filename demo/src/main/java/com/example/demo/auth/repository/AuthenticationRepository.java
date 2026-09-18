package com.example.demo.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.auth.model.User;

public interface AuthenticationRepository extends JpaRepository<User, Long> {

	public User findByEmailId(String email);

	public User findByEmailIdAndPassword(String email, String pass);

}
