package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.User;

public interface AuthenticationRepository extends JpaRepository<User, Long> {

	public User findByEmailId(String email);

	public User findByEmailIdAndPassword(String email, String pass);

}
