package com.example.demo.auth.port;

import java.util.List;
import java.util.Optional;

public interface UserReader {

    Optional<UserData> findByUserName(String userName);

    List<UserData> findAll();
}
