package com.example.demo.repository;

import com.example.demo.model.fantasy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    Boolean existsByUserName(String userName);
}
