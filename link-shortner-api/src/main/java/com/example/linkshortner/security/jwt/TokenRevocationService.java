package com.example.linkshortner.security.jwt;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TokenRevocationService {
    private volatile Instant lastRevocationTime = Instant.now();

    public void revokeAllToken() {
        lastRevocationTime = Instant.now();
    }

    public Instant getLastRevocationTime() {
        return lastRevocationTime;
    }
}
