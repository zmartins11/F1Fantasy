package com.example.f1.predict_service.contracts.user;

public record UserData(
        Integer id,
        String userName,
        String emailId,
        String password
) {
}
