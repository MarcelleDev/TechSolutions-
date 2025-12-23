package com.techsolutions.urlshortener.controller;

import com.techsolutions.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RedirectController {

    @Autowired
    private UrlShortenerService urlService;

    // Rota da Home: Carrega o seu HTML bonitão
    @GetMapping("/")
    public String home() {
        return "index"; // Retorna o arquivo index.html da pasta templates
    }

    // Rota de Redirecionamento: SÓ entra aqui se houver algo após a barra (ex: /abc123)
    @GetMapping("/{shortCode:[a-zA-Z0-9]+}")
    public String redirectToOriginalUrl(@PathVariable String shortCode) {
        try {
            String originalUrl = urlService.getOriginalUrl(shortCode);
            if (originalUrl != null && !originalUrl.isEmpty()) {
                return "redirect:" + originalUrl;
            }
            return "redirect:/error/not-found";
        } catch (Exception e) {
            return "redirect:/error/not-found";
        }
    }

    // Página de erro interna caso o link não exista
    @GetMapping("/error/not-found")
    @ResponseBody
    public String notFoundPage() {
        return "<html><body style='background:#0f172a; color:white; font-family:sans-serif; text-align:center; padding-top:100px;'>" +
               "<h1>404</h1><p>Ops! Esse link não existe.</p>" +
               "<a href='/' style='color:#2dd4bf;'>Voltar para a Home</a>" +
               "</body></html>";
    }
}