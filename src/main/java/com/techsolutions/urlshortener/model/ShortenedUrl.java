// Define que esta classe pertence ao pacote "model" dentro do nosso projeto
// Pacote = "pasta lógica" para organizar classes relacionadas
package com.techsolutions.urlshortener.model;

// Importações necessárias
import jakarta.persistence.*;       // Anotações do JPA para banco de dados
import java.time.LocalDateTime;    // Para trabalhar com datas/horas

// @Entity → Marca esta classe como uma ENTIDADE JPA
// Significa: "Esta classe representa uma tabela no banco de dados"
@Entity

// @Table → Especifica o nome da tabela no banco
// Se não especificar, usaria o nome da classe (ShortenedUrl)
@Table(name = "shortened_urls")
public class ShortenedUrl {
    
    // @Id → Marca este campo como CHAVE PRIMÁRIA da tabela
    // Cada registro terá um ID único
    @Id
    
    // @GeneratedValue → Faz o banco gerar o ID automaticamente
    // IDENTITY = usa auto-incremento (1, 2, 3, ...)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Ex: 1, 2, 3...
    
    // @Column → Configura como este campo será na tabela
    // short_code = nome da coluna
    // nullable = false → NÃO pode ser vazio (NOT NULL)
    // unique = true → Cada código deve ser único (não pode repetir)
    // length = 6 → Máximo 6 caracteres (ex: "abc123")
    @Column(name = "short_code", nullable = false, unique = true, length = 6)
    private String shortCode;  // Ex: "abc123"
    
    // original_url = URL longa que o usuário quer encurtar
    // columnDefinition = "TEXT" → Tipo TEXT no banco (para URLs longas)
    @Column(name = "original_url", nullable = false, columnDefinition = "TEXT")
    private String originalUrl;  // Ex: "https://www.google.com/search?q=..."
    
    // click_count = Quantas vezes a URL foi acessada
    // Inicia com 0 por padrão
    @Column(name = "click_count")
    private Long clickCount = 0L;  // Ex: 0, 1, 2, ...
    
    // created_at = Data/hora em que a URL foi criada
    @Column(name = "created_at")
    private LocalDateTime createdAt;  // Ex: 2025-12-21T18:30:00
    
    // expires_at = Data/hora em que a URL expira (opcional)
    // Pode ser null (não expira)
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;  // Ex: 2026-01-21T18:30:00 ou null
    
    // is_active = Se a URL está ativa ou não
    // Inicia como true (ativa) por padrão
    @Column(name = "is_active")
    private Boolean isActive = true;  // true = ativa, false = inativa
    
    // ------------------------------------------------------------
    // CONSTRUTORES
    // ------------------------------------------------------------
    
    // Construtor padrão (VAZIO) → OBRIGATÓRIO para o JPA funcionar
    // JPA precisa conseguir criar objetos sem parâmetros
    public ShortenedUrl() {
        this.createdAt = LocalDateTime.now();  // Data atual automaticamente
    }
    
    // Construtor com parâmetros → Para criar URLs facilmente
    // Ex: new ShortenedUrl("abc123", "https://google.com")
    public ShortenedUrl(String shortCode, String originalUrl) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.createdAt = LocalDateTime.now();  // Data atual
        this.clickCount = 0L;                  // Começa com 0 cliques
        this.isActive = true;                  // Começa ativa
    }
    
    // ------------------------------------------------------------
    // MÉTODOS DE COMPORTAMENTO
    // ------------------------------------------------------------
    
    // Método para aumentar o contador de cliques em 1
    // Será chamado toda vez que alguém acessar a URL curta
    public void incrementClickCount() {
        if (this.clickCount == null) {  // Se for null, inicia com 0
            this.clickCount = 0L;
        }
        this.clickCount++;  // Aumenta em 1
    }
    
    // ------------------------------------------------------------
    // GETTERS e SETTERS
    // ------------------------------------------------------------
    
    // GETTER → Permite LER o valor do campo
    // SETTER → Permite ALTERAR o valor do campo
    // São necessários para o JPA acessar os campos privados
    
    // Getter/Setter do ID
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    // Getter/Setter do código curto
    public String getShortCode() { return shortCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    
    // Getter/Setter da URL original
    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    
    // Getter/Setter do contador de cliques
    public Long getClickCount() { return clickCount; }
    public void setClickCount(Long clickCount) { this.clickCount = clickCount; }
    
    // Getter/Setter da data de criação
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // Getter/Setter da data de expiração
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    // Getter/Setter do status ativo/inativo
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}