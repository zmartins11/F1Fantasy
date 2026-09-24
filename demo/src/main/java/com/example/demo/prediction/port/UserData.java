package com.example.demo.prediction.port;

public record UserData(
        Integer id,
        String userName,
        String emailId,
        String password
) {
}
