package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateShortUrlRequest(
    @NotBlank(message = "originalUrl is required")
    @Size(max = 2048, message = "originalUrl must be at most 2048 characters")
    String originalUrl
) {
}
