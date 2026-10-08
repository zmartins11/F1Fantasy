package com.example.f1.auth_service.adapter;

import com.example.f1.auth_service.model.User;
import com.example.f1.auth_service.port.UserData;
import com.example.f1.auth_service.port.UserReader;
import com.example.f1.auth_service.service.AuthenticationService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserJpaAdapter implements UserReader {

    private final AuthenticationService authenticationService;

    public UserJpaAdapter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Optional<UserData> findByUserName(String userName) {
        return authenticationService.findByUsername(userName).map(this::toData);
    }

    @Override
    public List<UserData> findAll() {
        return authenticationService.findAll().stream().map(this::toData).toList();
    }

    private UserData toData (User user) {
        return new UserData(user.getId(),
                user.getUserName(),
                user.getEmailId(),
                user.getPassword());
    }
}
