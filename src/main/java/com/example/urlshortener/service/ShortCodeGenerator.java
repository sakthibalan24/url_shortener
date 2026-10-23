package com.example.urlshortener.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ShortCodeGenerator {

    private static final String ALPHABET =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int CODE_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    public String generate() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return code.toString();
    }
}
