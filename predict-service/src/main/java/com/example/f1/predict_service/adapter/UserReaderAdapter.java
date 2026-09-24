package com.example.f1.predict_service.adapter;

import com.example.f1.predict_service.contracts.user.UserData;
import com.example.f1.predict_service.contracts.user.UserReader;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserReaderAdapter implements UserReader {
    @Override
    public Optional<UserData> findByUserName(String name) {
        return Optional.empty();
    }
}
