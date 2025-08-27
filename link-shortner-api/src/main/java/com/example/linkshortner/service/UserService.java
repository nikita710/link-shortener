package com.example.linkshortner.service;

import com.example.linkshortner.dto.UserRequest;
import com.example.linkshortner.dto.UserResponse;
import com.example.linkshortner.model.User;
import com.example.linkshortner.repository.UserRepository;
import com.example.linkshortner.security.jwt.JwtService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public User findByUsername(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional
    public String register(UserRequest userRequest) {
        userRepository.findUserByEmail(userRequest.email()).ifPresent(user -> {
            throw new EntityExistsException("Email already exists");
        });

        User user = User.builder()
                .email(userRequest.email())
                .password(passwordEncoder.encode(userRequest.password()))
                .createdDate(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return "User registered successfully";
    }

    public UserResponse login(UserRequest userRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.email(), userRequest.password()));
        String token = jwtService.generateToken(userRequest.email(), Map.of("role", "USER"));
        return new UserResponse(token);
    }
}
