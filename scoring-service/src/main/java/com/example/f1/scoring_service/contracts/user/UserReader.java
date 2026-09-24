package com.example.f1.scoring_service.contracts.user;

import java.util.List;
import java.util.Optional;

public interface UserReader {

    Optional<UserData> findByUserName(String userName);

    List<UserData> findAll();
}
