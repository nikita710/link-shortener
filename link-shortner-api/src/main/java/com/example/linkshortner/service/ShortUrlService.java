package com.example.linkshortner.service;

import com.example.linkshortner.dto.ShortUrlRequest;
import com.example.linkshortner.dto.ShortUrlResponse;
import com.example.linkshortner.model.ShortUrl;
import com.example.linkshortner.repository.ShortUrlRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final ShortUrlGenerator shortUrlGenerator;

    @Value("${app.base-url}")
    private String baseUrl;

    @Transactional
    public ShortUrlResponse generateShortUrl(ShortUrlRequest shortUrlRequest) {
        String originalUrl = validateUrl(shortUrlRequest.originalUrl());
        String shortCode = null;
        String customCode = shortUrlRequest.customCode();

        if (!customCode.isEmpty() && validateCustomCode(customCode)) {
            shortCode = shortUrlRequest.customCode();
        } else {
            shortCode = generateShortCode();
        }
//todo: add user id
        ShortUrl shortUrl = ShortUrl.builder()
                .originalUrl(originalUrl)
                .shortenedCode(shortCode)
                .createdDate(LocalDateTime.now())
                .clickCount(0L)
                .build();

        ShortUrl savedShortUrl = shortUrlRepository.save(shortUrl);

        return new ShortUrlResponse(baseUrl + "/" + savedShortUrl.getShortenedCode());

    }

    public ShortUrl visitShortUrl(String shortenedCode) {
        ShortUrl shortUrl = shortUrlRepository.findByShortenedCode(shortenedCode)
                .orElseThrow(() -> new EntityNotFoundException("Shortened URL not found"));

        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        shortUrl.setLastAccessedAt(LocalDateTime.now());
        return shortUrlRepository.save(shortUrl);
    }

    private String validateUrl(String originalUrl) {
        try {
            URI uri = URI.create(originalUrl);
            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
                throw new IllegalArgumentException("Only http/https URLs are allowed");
            }
            if (uri.getHost() == null) throw new IllegalArgumentException("URL must have a host");
            if (originalUrl.length() > 2048) throw new IllegalArgumentException("URL too long");
            return originalUrl;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid URL: " + e.getMessage());
        }
    }

    private boolean validateCustomCode(String customCode) {
        if (customCode.length() < 6 || customCode.length() > 10)
            throw new IllegalArgumentException("Custom code must be between 6 and 10 characters");
        if (!customCode.matches("^[a-zA-Z0-9-_]+$"))
            throw new IllegalArgumentException("Custom code must contain only letters, numbers, and hyphens");
        if (shortUrlRepository.existsShortUrlByShortenedCode(customCode))
            throw new EntityExistsException("Custom code already exists");
        return true;
    }

    private String generateShortCode() {
        for (int len = 6; len <= 10; len++) {
            for (int attempts = 0; attempts < 3; attempts++) {
                String shortCode = shortUrlGenerator.generate(len);
                if (!shortUrlRepository.existsShortUrlByShortenedCode(shortCode)) return shortCode;
            }
        }
        throw new IllegalStateException("Unable to generate unique short code");
    }

}
