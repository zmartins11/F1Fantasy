package com.example.f1.scoring_service.contracts.user;

public record UserData(
        Integer id,
        String userName,
        String emailId,
        String password
) {
}