package com.example.f1.auth_service.port;

import java.util.List;
import java.util.Optional;

public interface UserReader {

    Optional<UserData> findByUserName(String userName);

    List<UserData> findAll();
}
