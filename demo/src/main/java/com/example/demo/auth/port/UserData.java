package com.example.demo.auth.port;

public record UserData(
        Integer id,
        String userName,
        String emailId,
        String password
) {
}
