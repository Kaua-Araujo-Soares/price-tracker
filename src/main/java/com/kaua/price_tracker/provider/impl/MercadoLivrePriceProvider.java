package com.kaua.price_tracker.provider.impl;

import com.kaua.price_tracker.provider.PriceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class MercadoLivrePriceProvider implements PriceProvider {

    private static final Pattern ITEM_ID_PATTERN = Pattern.compile("(MLB\\d+)", Pattern.CASE_INSENSITIVE);

    private final WebClient webClient;

    public MercadoLivrePriceProvider(@Value("${app.mercadolivre.api-url}") String apiUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .build();
    }

    @Override
    public BigDecimal fetchPrice(String url) {
        String itemId = extractItemId(url);

        if (itemId == null) {
            log.warn("[MercadoLivre] Não foi possível extrair o itemId da URL: {}", url);
            return null;
        }

        log.info("[MercadoLivre] Buscando preço para itemId: {}", itemId);

        try {
            MercadoLivreItemResponse response = webClient.get()
                    .uri("/{itemId}", itemId)
                    .retrieve()
                    .bodyToMono(MercadoLivreItemResponse.class)
                    .block();

            if (response == null || response.price() == null) {
                log.warn("[MercadoLivre] Resposta inválida para itemId: {}", itemId);
                return null;
            }

            log.info("[MercadoLivre] Preço obtido para {}: R$ {}", itemId, response.price());
            return response.price();

        } catch (Exception e) {
            log.error("[MercadoLivre] Erro ao buscar preço para itemId {}: {}", itemId, e.getMessage());
            return null;
        }
    }

    /**
     * Extrai o itemId (ex: MLB123456) de qualquer formato de URL do Mercado Livre.
     */
    private String extractItemId(String url) {
        if (url == null || url.isBlank()) return null;

        Matcher matcher = ITEM_ID_PATTERN.matcher(url);
        return matcher.find() ? matcher.group(1).toUpperCase() : null;
    }

    /**
     * DTO interno para desserializar a resposta da API do Mercado Livre.
     */
    private record MercadoLivreItemResponse(
            String id,
            String title,
            BigDecimal price,
            String currency_id
    ) {}
}
