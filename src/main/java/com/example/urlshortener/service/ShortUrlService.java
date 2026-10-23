package com.example.urlshortener.service;

import com.example.urlshortener.dto.ShortUrlResponse;
import com.example.urlshortener.entity.ShortUrl;
import com.example.urlshortener.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.NoSuchElementException;

@Service
public class ShortUrlService {

    private final ShortUrlRepository repository;
    private final ShortCodeGenerator codeGenerator;
    private final String baseUrl;

    public ShortUrlService(
        ShortUrlRepository repository,
        ShortCodeGenerator codeGenerator,
        @Value("${app.base-url:http://localhost:8080}") String baseUrl
    ) {
        this.repository = repository;
        this.codeGenerator = codeGenerator;
        this.baseUrl = baseUrl.replaceAll("/+$", "");
    }

    @Transactional
    public ShortUrlResponse create(String originalUrl) {
        URI uri;
        try {
            uri = URI.create(originalUrl.trim());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("originalUrl must be a valid URL");
        }

        String scheme = uri.getScheme();
        if (scheme == null
            || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))
            || uri.getHost() == null) {
            throw new IllegalArgumentException("Only valid HTTP or HTTPS URLs are allowed");
        }

        String normalizedOriginalUrl = uri.toASCIIString();

        // The database unique constraint is authoritative. Concurrent requests
        // can still collide even if we do a pre-check, so catch and retry.
        for (int attempt = 0; attempt < 10; attempt++) {
            String code = codeGenerator.generate();
            try {
                ShortUrl saved = repository.saveAndFlush(
                    new ShortUrl(code, normalizedOriginalUrl)
                );
                return new ShortUrlResponse(
                    saved.getShortCode(),
                    baseUrl + "/" + saved.getShortCode(),
                    saved.getOriginalUrl()
                );
            } catch (DataIntegrityViolationException collisionOrConstraintFailure) {
                // For this small learning app, retry generated-code collisions.
                // A production implementation should verify the violated
                // constraint before treating every integrity error as a collision.
            }
        }

        throw new IllegalStateException("Could not generate a unique short code. Please retry.");
    }

    @Transactional(readOnly = true)
    public String resolve(String shortCode) {
        return repository.findByShortCode(shortCode)
            .map(ShortUrl::getOriginalUrl)
            .orElseThrow(() -> new NoSuchElementException("Short URL not found"));
    }
}
