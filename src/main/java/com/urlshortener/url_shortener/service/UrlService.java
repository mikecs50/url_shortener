package com.urlshortener.url_shortener.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urlshortener.url_shortener.entity.ShortUrl;
import com.urlshortener.url_shortener.exception.ShortUrlNotFoundException;
import com.urlshortener.url_shortener.repository.ShortUrlRepository;

@Service 
public class UrlService {
    private ShortUrlRepository repository;

    @Autowired
    public UrlService(ShortUrlRepository repository) {
        this.repository = repository;
    }

    public String createShortUrl (String originalUrl) {
        if (originalUrl == null || originalUrl.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            throw new IllegalArgumentException("URL must start with http:// or https://");
        }

        String shortCode = generateUniqueShortCode();
        ShortUrl url = new ShortUrl(originalUrl, shortCode);
        repository.save(url);
        return shortCode;
    }

    public String getOriginalUrl(String shortCode){
        ShortUrl url = repository.findByShortCode(shortCode)
        .orElseThrow(() -> new ShortUrlNotFoundException("Short URL not found: " + shortCode));

        url.increaseCounter();
        repository.save(url);
        return url.getUrlOriginal();
    }

    private String generateUniqueShortCode() {
        for (int attempt = 0; attempt < 10; attempt++) {
            String code = generateRandomCode(6);
            if (repository.findByShortCode(code).isEmpty()) {
                return code;
            }
        }
        throw new IllegalStateException("Could not generate a unique short code");
    }

    private String generateRandomCode(int length) { 
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(new Random().nextInt(chars.length())));
        }
        return sb.toString();
    }
}
