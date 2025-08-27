package com.example.linkshortner.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ShortUrlGenerator {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM_INDEX_GENERATOR = new SecureRandom();

    public String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(RANDOM_INDEX_GENERATOR.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
