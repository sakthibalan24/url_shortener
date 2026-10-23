package com.example.urlshortener.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShortCodeGeneratorTest {

    @Test
    void generatesSixCharacterAlphanumericCode() {
        ShortCodeGenerator generator = new ShortCodeGenerator();

        String code = generator.generate();

        assertEquals(6, code.length());
        assertTrue(code.matches("[A-Za-z0-9]{6}"));
    }
}
