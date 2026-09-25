package com.example.f1.auth_service.repository;


import com.example.f1.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<User, Long> {

	public User findByEmailId(String email);

	public User findByEmailIdAndPassword(String email, String pass);

}
