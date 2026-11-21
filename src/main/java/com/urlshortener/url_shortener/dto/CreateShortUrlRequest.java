package com.urlshortener.url_shortener.dto;

public class CreateShortUrlRequest {
    private String url;

    public CreateShortUrlRequest() {}
    
    public CreateShortUrlRequest(String url){
        this.url=url;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl (String url){
        this.url=url;
    }
}
