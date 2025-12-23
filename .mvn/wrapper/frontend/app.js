// JavaScript SIMPLIFICADO - VERSÃO FUNCIONAL
document.addEventListener('DOMContentLoaded', function() {
    // Elementos do DOM
    const urlInput = document.getElementById('urlInput');
    const shortenBtn = document.getElementById('shortenBtn');
    const resultsSection = document.getElementById('resultsSection');
    const urlList = document.getElementById('urlList');
    
    // Configuração
    const API_URL = '/api/urls';
    
    // Event Listeners
    shortenBtn.addEventListener('click', shortenUrl);
    urlInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') shortenUrl();
    });
    
    // Carregar URLs ao iniciar
    loadUrls();
    
    // Função para encurtar URL
    async function shortenUrl() {
        const url = urlInput.value.trim();
        
        if (!url) {
            alert('Por favor, insira uma URL');
            return;
        }
        
        // Validar URL
        try {
            new URL(url);
        } catch {
            alert('URL inválida. Use http:// ou https://');
            return;
        }
        
        // Mostrar loading
        shortenBtn.disabled = true;
        shortenBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Encurtando...';
        
        try {
            const response = await fetch(API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    originalUrl: url
                })
            });
            
            if (!response.ok) {
                throw new Error('Erro ao encurtar URL');
            }
            
            const data = await response.json();
            
            // Limpar input
            urlInput.value = '';
            
            // Mostrar resultados
            resultsSection.classList.remove('hidden');
            
            // Adicionar à lista
            addUrlToList(data);
            
            // Recarregar lista
            await loadUrls();
            
            // Mostrar mensagem
            showMessage('URL encurtada com sucesso!', 'success');
            
        } catch (error) {
            console.error('Erro:', error);
            showMessage('Erro ao encurtar URL', 'error');
        } finally {
            shortenBtn.disabled = false;
            shortenBtn.innerHTML = '<i class="fas fa-magic"></i> Encurtar URL';
        }
    }
    
    // Função para carregar URLs
    async function loadUrls() {
        try {
            const response = await fetch(API_URL);
            const urls = await response.json();
            
            // Limpar lista
            urlList.innerHTML = '';
            
            // Adicionar URLs (da mais nova para a mais antiga)
            urls.reverse().forEach(url => addUrlToList(url));
            
            // Mostrar/ocultar seção
            if (urls.length > 0) {
                resultsSection.classList.remove('hidden');
            } else {
                resultsSection.classList.add('hidden');
            }
            
        } catch (error) {
            console.error('Erro ao carregar URLs:', error);
        }
    }
    
    // Função para adicionar URL à lista
    function addUrlToList(urlData) {
        const urlElement = document.createElement('div');
        urlElement.className = 'url-item';
        
        const shortUrl = `${window.location.origin}/${urlData.shortCode}`;
        
        urlElement.innerHTML = `
            <div class="short-url">
                <a href="${shortUrl}" target="_blank">${shortUrl}</a>
            </div>
            <div class="original-url" title="${urlData.originalUrl}">
                ${urlData.originalUrl}
            </div>
            <div class="url-actions">
                <button class="btn-copy" onclick="copyToClipboard('${shortUrl}')">
                    <i class="far fa-copy"></i> Copiar
                </button>
            </div>
        `;
        
        urlList.prepend(urlElement);
    }
    
    // Função para mostrar mensagens
    function showMessage(message, type) {
        const alertDiv = document.createElement('div');
        alertDiv.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 15px 20px;
            background: ${type === 'success' ? '#10b981' : '#ef4444'};
            color: white;
            border-radius: 8px;
            z-index: 1000;
            animation: slideIn 0.3s ease;
        `;
        
        alertDiv.textContent = message;
        document.body.appendChild(alertDiv);
        
        setTimeout(() => {
            alertDiv.remove();
        }, 3000);
    }
    
    // Adicionar CSS para animação
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideIn {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }
    `;
    document.head.appendChild(style);
});

// Função global para copiar
function copyToClipboard(text) {
    navigator.clipboard.writeText(text)
        .then(() => {
            alert('Link copiado!');
        })
        .catch(err => {
            console.error('Erro ao copiar:', err);
            alert('Erro ao copiar');
        });
}