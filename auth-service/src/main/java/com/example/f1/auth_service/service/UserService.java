package com.example.f1.auth_service.service;

import com.example.f1.auth_service.model.User;
import com.example.f1.auth_service.port.UserData;
import com.example.f1.auth_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserData> findAllUsers() {
        return toUserData(userRepository.findAll());
    }

    private List<UserData> toUserData(List<User> listUsers) {
        if (listUsers == null) {
            return Collections.emptyList();
        }

        return listUsers.stream()
                .map(user -> new UserData(
                        user.getId(),
                        user.getUserName(),
                        user.getEmailId(),
                        null

                ))
                .toList();
    }
}
