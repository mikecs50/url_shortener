package com.urlshortener.url_shortener.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urlshortener.url_shortener.dto.CreateShortUrlRequest;
import com.urlshortener.url_shortener.dto.CreateShortUrlResponse;
import com.urlshortener.url_shortener.exception.ShortUrlNotFoundException;
import com.urlshortener.url_shortener.service.UrlService;

@RestController
@RequestMapping("/")
public class UrlController {
    private UrlService service;
    
    @Autowired
    public UrlController(UrlService service) {
        this.service = service;
    }

    @PostMapping("/api/shorten")
    public CreateShortUrlResponse shorten(@RequestBody CreateShortUrlRequest request) {
        String shortCode = service.createShortUrl(request.getUrl());
        CreateShortUrlResponse response = new CreateShortUrlResponse(shortCode, request.getUrl());
        return response;
    }

    @GetMapping("/s/{shortCode}")
    public ResponseEntity<?> redirect(@PathVariable String shortCode) {
        String originalUrl = service.getOriginalUrl(shortCode);
        // Redireciona com 302
        return ResponseEntity.status(302).location(URI.create(originalUrl)).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequest(IllegalArgumentException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ShortUrlNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ShortUrlNotFoundException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
