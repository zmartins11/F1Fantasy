package com.example.f1.auth_service.port;

public record UserData(
        Integer id,
        String userName,
        String emailId,
        String password
) {
}