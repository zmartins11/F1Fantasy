package com.example.f1.auth_service.controller;

import com.example.f1.auth_service.port.UserData;
import com.example.f1.auth_service.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {

    private final UserService userService;

    public InternalUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserData> findAll() {
        return userService.findAllUsers();
    }
}
