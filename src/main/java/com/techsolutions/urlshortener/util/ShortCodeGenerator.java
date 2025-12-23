package com.techsolutions.urlshortener.util;

// Importa RandomStringUtils do Apache Commons
// Esta classe vai nos ajudar a gerar strings aleatórias
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

// @Component → Marca esta classe como um COMPONENTE Spring
// Pode ser injetado em outras classes com @Autowired
@Component
public class ShortCodeGenerator {
    
    // ------------------------------------------------------------
    // CONSTANTES
    // ------------------------------------------------------------
    
    // Caracteres permitidos nos códigos curtos
    // Letras maiúsculas + letras minúsculas + números
    // Ex: ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789
    private static final String CHARACTERS = 
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    
    // Tamanho padrão do código (6 caracteres)
    // 6 caracteres = 62^6 = ~56 bilhões de combinações possíveis!
    private static final int DEFAULT_LENGTH = 6;
    
    // ------------------------------------------------------------
    // MÉTODO PRINCIPAL
    // ------------------------------------------------------------
    
    /**
     * Gera um código curto aleatório de 6 caracteres.
     * Exemplos: "aB3dEf", "Xy7Z9q", "123AbC"
     * 
     * @return Código curto aleatório
     */
    public String generate() {
        // RandomStringUtils.random() gera uma string aleatória
        // Parâmetros:
        // 1. tamanho (6)
        // 2. caracteres permitidos (CHARACTERS)
        return RandomStringUtils.random(DEFAULT_LENGTH, CHARACTERS);
    }
    
    /**
     * Gera um código curto aleatório com tamanho personalizado.
     * 
     * @param length Tamanho desejado do código
     * @return Código curto aleatório com o tamanho especificado
     */
    public String generate(int length) {
        // Validação básica
        if (length <= 0) {
            throw new IllegalArgumentException("O tamanho deve ser maior que zero");
        }
        
        return RandomStringUtils.random(length, CHARACTERS);
    }
    
    // ------------------------------------------------------------
    // MÉTODO PARA VALIDAR CÓDIGOS
    // ------------------------------------------------------------
    
    /**
     * Verifica se um código é válido (só contém caracteres permitidos).
     * 
     * @param code Código a ser validado
     * @return true se válido, false se inválido
     */
    public boolean isValidCode(String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }
        
        // Verifica cada caractere do código
        for (char c : code.toCharArray()) {
            // Se algum caractere NÃO estiver na lista de permitidos → inválido
            if (CHARACTERS.indexOf(c) == -1) {
                return false;
            }
        }
        
        return true;
    }
    
    // ------------------------------------------------------------
    // MÉTODO ESTÁTICO (alternativo sem precisar do Spring)
    // ------------------------------------------------------------
    
    /**
     * Gera um código curto (método estático, não precisa de @Autowired).
     * Útil para usar em testes ou sem injeção de dependências.
     * 
     * @return Código curto aleatório
     */
    public static String generateStatic() {
        return RandomStringUtils.random(DEFAULT_LENGTH, CHARACTERS);
    }
}