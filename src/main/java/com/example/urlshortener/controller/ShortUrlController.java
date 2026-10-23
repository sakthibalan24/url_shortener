package com.example.urlshortener.controller;

import com.example.urlshortener.dto.CreateShortUrlRequest;
import com.example.urlshortener.dto.ShortUrlResponse;
import com.example.urlshortener.service.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class ShortUrlController {

    private final ShortUrlService service;

    public ShortUrlController(ShortUrlService service) {
        this.service = service;
    }

    @PostMapping("/api/urls")
    public ResponseEntity<ShortUrlResponse> create(
        @Valid @RequestBody CreateShortUrlRequest request
    ) {
        ShortUrlResponse response = service.create(request.originalUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortCode:[A-Za-z0-9]{6}}")
    public RedirectView redirect(@PathVariable String shortCode) {
        String originalUrl = service.resolve(shortCode);
        RedirectView view = new RedirectView(originalUrl);
        view.setStatusCode(HttpStatus.FOUND); // HTTP 302
        view.setExposeModelAttributes(false);
        return view;
    }
}
