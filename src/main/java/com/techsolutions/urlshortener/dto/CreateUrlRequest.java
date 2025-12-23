package com.techsolutions.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateUrlRequest {
    
    @NotBlank(message = "A URL original é obrigatória")
    @Pattern(
        regexp = "^https?://.+$", 
        message = "URL inválida. Deve começar com http:// ou https://"
    )
    private String originalUrl;
    
    @Size(max = 2048, message = "URL muito longa (máximo 2048 caracteres)")
    private String customCode;
    
    private Integer expiresInDays;
    
    public CreateUrlRequest() {
    }
    
    public CreateUrlRequest(String originalUrl) {
        this.originalUrl = originalUrl;
    }
    
    public String getOriginalUrl() {
        return originalUrl;
    }
    
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
    
    public String getCustomCode() {
        return customCode;
    }
    
    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }
    
    public Integer getExpiresInDays() {
        return expiresInDays;
    }
    
    public void setExpiresInDays(Integer expiresInDays) {
        this.expiresInDays = expiresInDays;
    }
    
    @Override
    public String toString() {
        return "CreateUrlRequest{" +
                "originalUrl='" + originalUrl + '\'' +
                ", customCode='" + customCode + '\'' +
                ", expiresInDays=" + expiresInDays +
                '}';
    }
}