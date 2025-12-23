package com.techsolutions.urlshortener.service;

// Importa nossos componentes
import com.techsolutions.urlshortener.dto.CreateUrlRequest;
import com.techsolutions.urlshortener.dto.UrlResponse;
import com.techsolutions.urlshortener.model.ShortenedUrl;
import com.techsolutions.urlshortener.repository.UrlRepository;
import com.techsolutions.urlshortener.util.ShortCodeGenerator;

// Importa do Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Importa utilitários
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// @Service → Marca esta classe como um SERVIÇO Spring
// Contém a lógica de negócio da aplicação
@Service
public class UrlShortenerService {
    
    // ------------------------------------------------------------
    // INJEÇÃO DE DEPENDÊNCIAS (Spring injeta automaticamente)
    // ------------------------------------------------------------
    
    @Autowired
    private UrlRepository urlRepository;
    
    @Autowired
    private ShortCodeGenerator codeGenerator;
    
    // @Value → Injeta valor do application.yml
    @Value("${app.base-url}")
    private String baseUrl;
    
    @Value("${app.short-code-length:6}")
    private int shortCodeLength;
    
    // ------------------------------------------------------------
    // MÉTODO PRINCIPAL: Criar URL encurtada
    // ------------------------------------------------------------
    
    /**
     * Cria uma nova URL encurtada
     * 
     * @param request Dados da URL a ser encurtada
     * @return Resposta com dados da URL criada
     * @throws IllegalArgumentException Se o código customizado já existir
     */
    @Transactional
    public UrlResponse createShortUrl(CreateUrlRequest request) {
        String shortCode;
        
        // Verifica se o usuário forneceu um código customizado
        if (request.getCustomCode() != null && !request.getCustomCode().isBlank()) {
            shortCode = request.getCustomCode();
            
            // Valida se o código customizado é válido
            if (!codeGenerator.isValidCode(shortCode)) {
                throw new IllegalArgumentException(
                    "Código customizado inválido. Use apenas letras e números."
                );
            }
            
            // Verifica se o código customizado já existe
            if (urlRepository.existsByShortCode(shortCode)) {
                throw new IllegalArgumentException(
                    "Código customizado '" + shortCode + "' já está em uso."
                );
            }
        } else {
            // Gera código aleatório até encontrar um disponível
            shortCode = generateUniqueShortCode();
        }
        
        // Cria a entidade para salvar no banco
        ShortenedUrl shortenedUrl = new ShortenedUrl(shortCode, request.getOriginalUrl());
        
        // Configura expiração se fornecida
        if (request.getExpiresInDays() != null && request.getExpiresInDays() > 0) {
            LocalDateTime expiresAt = LocalDateTime.now()
                .plusDays(request.getExpiresInDays());
            shortenedUrl.setExpiresAt(expiresAt);
        }
        
        // Salva no banco de dados
        ShortenedUrl savedUrl = urlRepository.save(shortenedUrl);
        
        // Converte para DTO de resposta
        return convertToResponse(savedUrl);
    }
    
    // ------------------------------------------------------------
    // MÉTODO: Redirecionar (buscar URL original)
    // ------------------------------------------------------------
    
    /**
     * Busca URL original pelo código curto e incrementa contador
     * 
     * @param shortCode Código curto (ex: "abc123")
     * @return URL original se encontrada e ativa
     * @throws RuntimeException Se URL não for encontrada ou expirada
     */
    @Transactional
    public String getOriginalUrl(String shortCode) {
        // Busca no banco
        Optional<ShortenedUrl> urlOpt = urlRepository.findByShortCode(shortCode);
        
        if (urlOpt.isEmpty()) {
            throw new RuntimeException("URL não encontrada para o código: " + shortCode);
        }
        
        ShortenedUrl url = urlOpt.get();
        
        // Verifica se está ativa
        if (!url.getIsActive()) {
            throw new RuntimeException("URL está desativada: " + shortCode);
        }
        
        // Verifica se expirou
        if (url.getExpiresAt() != null && url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("URL expirada: " + shortCode);
        }
        
        // Incrementa contador de cliques
        urlRepository.incrementClickCount(shortCode);
        
        // Retorna URL original
        return url.getOriginalUrl();
    }
    
    // ------------------------------------------------------------
    // MÉTODO: Listar todas URLs
    // ------------------------------------------------------------
    
    /**
     * Retorna todas as URLs encurtadas
     * 
     * @return Lista de URLs
     */
    public List<UrlResponse> getAllUrls() {
        return urlRepository.findAll().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    // ------------------------------------------------------------
    // MÉTODO: Buscar URL por código
    // ------------------------------------------------------------
    
    /**
     * Busca detalhes de uma URL específica
     * 
     * @param shortCode Código curto
     * @return Detalhes da URL
     */
    public UrlResponse getUrlDetails(String shortCode) {
        ShortenedUrl url = urlRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new RuntimeException("URL não encontrada: " + shortCode));
        
        return convertToResponse(url);
    }
    
    // ------------------------------------------------------------
    // MÉTODO: Desativar URL
    // ------------------------------------------------------------
    
    /**
     * Desativa uma URL (soft delete)
     * 
     * @param shortCode Código curto
     */
    @Transactional
    public void deactivateUrl(String shortCode) {
        ShortenedUrl url = urlRepository.findByShortCode(shortCode)
            .orElseThrow(() -> new RuntimeException("URL não encontrada: " + shortCode));
        
        url.setIsActive(false);
        urlRepository.save(url);
    }
    
    // ------------------------------------------------------------
    // MÉTODO PRIVADO: Gerar código único
    // ------------------------------------------------------------
    
    /**
     * Gera um código curto único (não existente no banco)
     * 
     * @return Código único
     */
    private String generateUniqueShortCode() {
        String shortCode;
        int attempts = 0;
        final int MAX_ATTEMPTS = 10;
        
        do {
            shortCode = codeGenerator.generate(shortCodeLength);
            attempts++;
            
            if (attempts >= MAX_ATTEMPTS) {
                throw new RuntimeException(
                    "Não foi possível gerar um código único após " + MAX_ATTEMPTS + " tentativas."
                );
            }
            
        } while (urlRepository.existsByShortCode(shortCode));
        
        return shortCode;
    }
    
    // ------------------------------------------------------------
    // MÉTODO PRIVADO: Converter entidade para DTO
    // ------------------------------------------------------------
    
    /**
     * Converte uma entidade ShortenedUrl para UrlResponse
     * 
     * @param url Entidade do banco
     * @return DTO para resposta
     */
    private UrlResponse convertToResponse(ShortenedUrl url) {
        UrlResponse response = new UrlResponse();
        response.setShortCode(url.getShortCode());
        response.setOriginalUrl(url.getOriginalUrl());
        response.setShortUrl(baseUrl + "/" + url.getShortCode());
        response.setClickCount(url.getClickCount());
        response.setCreatedAt(url.getCreatedAt());
        response.setExpiresAt(url.getExpiresAt());
        response.setIsActive(url.getIsActive());
        
        return response;
    }
}