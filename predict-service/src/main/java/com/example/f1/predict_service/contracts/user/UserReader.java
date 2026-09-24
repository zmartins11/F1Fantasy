package com.example.f1.predict_service.contracts.user;

import java.util.Optional;

public interface UserReader {

    Optional<UserData> findByUserName(String name);
}
