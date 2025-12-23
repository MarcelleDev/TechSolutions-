package com.techsolutions.urlshortener.dto;

import java.time.LocalDateTime;

/**
 * DTO para retornar dados da API
 * Quando alguém acessa GET /api/urls/{code}, retornamos este objeto
 */
public class UrlResponse {
    
    private String shortCode;          // Código curto: "abc123"
    private String originalUrl;        // URL original
    private String shortUrl;           // URL completa curta: "http://localhost:8080/abc123"
    private Long clickCount;           // Quantidade de cliques
    private LocalDateTime createdAt;   // Data de criação
    private LocalDateTime expiresAt;   // Data de expiração (pode ser null)
    private Boolean isActive;          // Se está ativa
    
    // ------------------------------------------------------------
    // CONSTRUTORES
    // ------------------------------------------------------------
    
    public UrlResponse() {
    }
    
    // Construtor com parâmetros principais
    public UrlResponse(String shortCode, String originalUrl, String baseUrl) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.shortUrl = baseUrl + "/" + shortCode;
        this.clickCount = 0L;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }
    
    // ------------------------------------------------------------
    // GETTERS e SETTERS
    // ------------------------------------------------------------
    
    public String getShortCode() {
        return shortCode;
    }
    
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
    
    public String getOriginalUrl() {
        return originalUrl;
    }
    
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
    
    public String getShortUrl() {
        return shortUrl;
    }
    
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
    
    public Long getClickCount() {
        return clickCount;
    }
    
    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    // ------------------------------------------------------------
    // MÉTODO toString
    // ------------------------------------------------------------
    
    @Override
    public String toString() {
        return "UrlResponse{" +
                "shortCode='" + shortCode + '\'' +
                ", originalUrl='" + originalUrl + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                ", clickCount=" + clickCount +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", isActive=" + isActive +
                '}';
    }
}