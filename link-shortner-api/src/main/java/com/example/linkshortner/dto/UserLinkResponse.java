package com.example.linkshortner.dto;

import java.time.LocalDateTime;

public record UserLinkResponse(String shortLink, String originalUrl, Long clickCount, LocalDateTime createdDate,
                               LocalDateTime lastAccessedAt) {
}
