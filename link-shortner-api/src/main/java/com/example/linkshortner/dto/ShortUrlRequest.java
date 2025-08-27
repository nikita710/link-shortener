package com.example.linkshortner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ShortUrlRequest(
        @NotEmpty(message = "Original URL is required")
        @NotBlank(message = "Original URL is required")
        String originalUrl,
        String customCode

) {
}
