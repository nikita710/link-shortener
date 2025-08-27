package com.example.linkshortner.controller;

import com.example.linkshortner.dto.UserRequest;
import com.example.linkshortner.dto.UserResponse;
import com.example.linkshortner.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(userService.register(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(userService.login(userRequest));
    }
}
