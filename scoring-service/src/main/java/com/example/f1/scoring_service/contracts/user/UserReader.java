package com.example.f1.scoring_service.contracts.user;

import java.util.List;
import java.util.Optional;

public interface UserReader {

    List<UserData> findAll();
}
