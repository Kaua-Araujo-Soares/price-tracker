package com.kaua.price_tracker.provider;

import java.math.BigDecimal;

public interface PriceProvider {

    /**
     * Busca o preço atual de um produto a partir da URL informada.
     *
     * @param url URL do produto na loja
     * @return preço atual como BigDecimal, ou null se não for possível obter
     */
    BigDecimal fetchPrice(String url);
}
