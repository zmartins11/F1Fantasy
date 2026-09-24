package com.example.f1.scoring_service.adapter;

import com.example.f1.scoring_service.contracts.user.UserData;
import com.example.f1.scoring_service.contracts.user.UserReader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRestReader implements UserReader {

    @Override
    public Optional<UserData> findByUserName(String userName) {
        return Optional.empty();
    }

    @Override
    public List<UserData> findAll() {
        return null;
    }
}
