package com.example.demo.auth.adapter;

import com.example.demo.auth.model.User;
import com.example.demo.auth.port.UserData;
import com.example.demo.auth.port.UserReader;
import com.example.demo.auth.service.AuthenticationService;
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
