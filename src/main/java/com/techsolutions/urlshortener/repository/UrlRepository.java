package com.techsolutions.urlshortener.repository;

// Importa nossa entidade
import com.techsolutions.urlshortener.model.ShortenedUrl;

// Importa do Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// @Repository → Marca esta interface como um REPOSITÓRIO Spring
// Spring cria automaticamente uma implementação
@Repository
public interface UrlRepository extends JpaRepository<ShortenedUrl, Long> {
    
    // ------------------------------------------------------------
    // MÉTODOS AUTOMÁTICOS DO SPRING DATA JPA
    // ------------------------------------------------------------
    
    // Spring cria automaticamente: SELECT * FROM shortened_urls WHERE short_code = ?
    // Retorna Optional (pode ter ou não resultado)
    Optional<ShortenedUrl> findByShortCode(String shortCode);
    
    // Spring cria automaticamente: SELECT COUNT(*) FROM shortened_urls WHERE short_code = ?
    // Retorna true se existir, false se não existir
    boolean existsByShortCode(String shortCode);
    
    // ------------------------------------------------------------
    // MÉTODO PERSONALIZADO COM QUERY JPQL
    // ------------------------------------------------------------
    
    // @Modifying → Indica que este método MODIFICA dados (UPDATE/DELETE)
    // @Transactional → Garante que a operação seja atômica (tudo ou nada)
    // @Query → Define uma consulta JPQL personalizada
    
    // Esta query aumenta o click_count em 1 para uma URL específica
    // ":shortCode" é um parâmetro que será substituído
    @Modifying
    @Transactional
    @Query("UPDATE ShortenedUrl u SET u.clickCount = u.clickCount + 1 WHERE u.shortCode = :shortCode")
    void incrementClickCount(@Param("shortCode") String shortCode);
    
    // ------------------------------------------------------------
    // MÉTODOS JÁ HERDADOS DE JpaRepository (NÃO PRECISA ESCREVER):
    // ------------------------------------------------------------
    // save(ShortenedUrl) → Salva uma nova URL ou atualiza existente
    // findById(Long) → Busca por ID
    // findAll() → Retorna todas as URLs
    // deleteById(Long) → Remove uma URL por ID
    // count() → Conta quantas URLs existem
    // ... e muitos outros!
}