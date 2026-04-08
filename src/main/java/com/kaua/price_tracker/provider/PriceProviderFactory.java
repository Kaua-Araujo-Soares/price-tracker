package com.kaua.price_tracker.provider;

import com.kaua.price_tracker.model.Provider;
import com.kaua.price_tracker.provider.impl.MercadoLivrePriceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriceProviderFactory {

    private final MercadoLivrePriceProvider mercadoLivrePriceProvider;

    /**
     * Retorna o PriceProvider correspondente ao provider informado.
     * Retorna null para providers ainda não suportados (AMAZON, OTHER).
     */
    public PriceProvider getProvider(Provider provider) {
        return switch (provider) {
            case MERCADO_LIVRE -> mercadoLivrePriceProvider;
            case AMAZON, OTHER -> null;
        };
    }
}
