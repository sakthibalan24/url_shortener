package com.example.urlshortener.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "short_urls",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_short_urls_short_code",
        columnNames = "short_code"
    )
)
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_code", nullable = false, unique = true, length = 16)
    private String shortCode;

    @Column(name = "original_url", nullable = false, length = 2048)
    private String originalUrl;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    protected ShortUrl() {
        // Required by JPA
    }

    public ShortUrl(String shortCode, String originalUrl) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
    }

    public Long getId() {
        return id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
