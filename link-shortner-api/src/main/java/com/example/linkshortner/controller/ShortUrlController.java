package com.example.linkshortner.controller;


import com.example.linkshortner.dto.ShortUrlRequest;
import com.example.linkshortner.dto.ShortUrlResponse;
import com.example.linkshortner.dto.UserLinkResponse;
import com.example.linkshortner.model.ShortUrl;
import com.example.linkshortner.service.ShortUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortUrlResponse> shortenUrl(@Valid @RequestBody ShortUrlRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(shortUrlService.generateShortUrl(request, userDetails), HttpStatus.CREATED);
    }

    @GetMapping("/{shortenedCode}")
    public ResponseEntity<String> redirectToOriginalUrl(@PathVariable String shortenedCode) {
        ShortUrl shortUrl = shortUrlService.visitShortUrl(shortenedCode);
        return ResponseEntity.status(302).location(URI.create(shortUrl.getOriginalUrl())).build();
    }

    @GetMapping("/user/links")
    public ResponseEntity<List<UserLinkResponse>> getUserLinks(@AuthenticationPrincipal UserDetails userDetails,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(shortUrlService.getUserLinks(userDetails, PageRequest.of(page, size)));
    }

    @GetMapping("/user/short_link/{shortenedCode}/analytics")
    public ResponseEntity<UserLinkResponse> getShortLinkAnalytics(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @PathVariable String shortenedCode) {
        return ResponseEntity.ok(shortUrlService.getShortLinkAnalytics(userDetails, shortenedCode));
    }

}
