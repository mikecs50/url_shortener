package com.urlshortener.url_shortener.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "short_urls")
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column (name = "url_original")
    private String urlOriginal;

    @Column (name = "short_code")
    private String shortCode;

    @Column (name = "created_at")
    private LocalDateTime createdAt;

    @Column (name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @Column (name = "counter")
    private int counter;

    public ShortUrl() {}

    public ShortUrl(String urlOriginal, String shortCode) {
        this.urlOriginal = urlOriginal;
        this.shortCode = shortCode;
        this.createdAt = LocalDateTime.now();
        this.lastAccessedAt = null;
        this.counter = 0;
    }   
    
    public ShortUrl(UUID id, String urlOriginal, String shortCode, LocalDateTime createdAt, LocalDateTime lastAccessedAt, int counter) {
        this.id = id;
        this.urlOriginal = urlOriginal;
        this.shortCode = shortCode;
        this.createdAt = createdAt;
        this.lastAccessedAt = lastAccessedAt;
        this.counter = counter;
    }

    public UUID getId() { return id; }

    public String getUrlOriginal(){
        return urlOriginal;
    }

    public void setUrlOriginal(String url){
        urlOriginal = url;
    }

    public String getShortCode(){
        return shortCode;
    }

    public void setShortCode(String shortCode){
        this.shortCode = shortCode;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public LocalDateTime getLastAccessedAt(){
        return lastAccessedAt;
    }

    public void updateLastAccessed() {
    this.lastAccessedAt = LocalDateTime.now();
    }   

    public int getCounter(){
        return counter;
    }

    public void increaseCounter(){
        updateLastAccessed();
        counter++;
    }
}

