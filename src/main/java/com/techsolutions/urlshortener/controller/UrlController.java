package com.techsolutions.urlshortener.controller;

import com.techsolutions.urlshortener.dto.CreateUrlRequest;
import com.techsolutions.urlshortener.dto.UrlResponse;
import com.techsolutions.urlshortener.service.UrlShortenerService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController → Marca esta classe como um Controller REST
// @RequestMapping("/api/urls") → Define o prefixo para todos os endpoints
@RestController
@RequestMapping("/api/urls")
public class UrlController {
    
    @Autowired
    private UrlShortenerService urlService;
    
    // ------------------------------------------------------------
    // POST /api/urls → Criar nova URL encurtada
    // ------------------------------------------------------------
    
    /**
     * Cria uma nova URL encurtada
     * Exemplo: POST /api/urls
     * Body: {"originalUrl": "https://www.google.com"}
     */
    @PostMapping
    public ResponseEntity<UrlResponse> createShortUrl(@Valid @RequestBody CreateUrlRequest request) {
        try {
            UrlResponse response = urlService.createShortUrl(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // Se for código customizado duplicado ou inválido
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    // ------------------------------------------------------------
    // GET /api/urls → Listar todas URLs
    // ------------------------------------------------------------
    
    /**
     * Lista todas as URLs encurtadas
     * Exemplo: GET /api/urls
     */
    @GetMapping
    public ResponseEntity<List<UrlResponse>> getAllUrls() {
        List<UrlResponse> urls = urlService.getAllUrls();
        return ResponseEntity.ok(urls);
    }
    
    // ------------------------------------------------------------
    // GET /api/urls/{code} → Ver detalhes de uma URL
    // ------------------------------------------------------------
    
    /**
     * Obtém detalhes de uma URL específica
     * Exemplo: GET /api/urls/abc123
     */
    @GetMapping("/{code}")
    public ResponseEntity<UrlResponse> getUrlDetails(@PathVariable String code) {
        try {
            UrlResponse response = urlService.getUrlDetails(code);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    // ------------------------------------------------------------
    // DELETE /api/urls/{code} → Desativar uma URL
    // ------------------------------------------------------------
    
    /**
     * Desativa uma URL (soft delete)
     * Exemplo: DELETE /api/urls/abc123
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deactivateUrl(@PathVariable String code) {
        try {
            urlService.deactivateUrl(code);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    // ------------------------------------------------------------
    // GET /api/urls/{code}/clicks → Apenas contador de cliques
    // ------------------------------------------------------------
    
    /**
     * Obtém apenas o contador de cliques de uma URL
     * Exemplo: GET /api/urls/abc123/clicks
     */
    @GetMapping("/{code}/clicks")
    public ResponseEntity<Long> getClickCount(@PathVariable String code) {
        try {
            UrlResponse response = urlService.getUrlDetails(code);
            return ResponseEntity.ok(response.getClickCount());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}