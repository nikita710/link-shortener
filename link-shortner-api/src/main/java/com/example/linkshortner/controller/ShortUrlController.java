package com.example.linkshortner.controller;


import com.example.linkshortner.dto.ShortUrlRequest;
import com.example.linkshortner.dto.ShortUrlResponse;
import com.example.linkshortner.model.ShortUrl;
import com.example.linkshortner.service.ShortUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortUrlResponse> shortenUrl(@Valid @RequestBody ShortUrlRequest request) {
        return new ResponseEntity<>(shortUrlService.generateShortUrl(request), HttpStatus.CREATED);
    }

    @GetMapping("/{shortenedCode}")
    public ResponseEntity<String> redirectToOriginalUrl(@PathVariable String shortenedCode) {
        ShortUrl shortUrl = shortUrlService.visitShortUrl(shortenedCode);
        return ResponseEntity.status(302).location(URI.create(shortUrl.getOriginalUrl())).build();
    }
}
