package com.kaua.price_tracker.scheduler;

import com.kaua.price_tracker.model.PriceHistory;
import com.kaua.price_tracker.model.Product;
import com.kaua.price_tracker.provider.PriceProvider;
import com.kaua.price_tracker.provider.PriceProviderFactory;
import com.kaua.price_tracker.repository.PriceHistoryRepository;
import com.kaua.price_tracker.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceFetchScheduler {

    private final ProductRepository productRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final PriceProviderFactory priceProviderFactory;

    @Scheduled(fixedDelayString = "${app.scheduler.price-fetch-interval}")
    public void fetchPrices() {
        log.info("[PriceFetch] Iniciando busca de preços...");

        List<Product> products = productRepository.findAll();
        log.info("[PriceFetch] {} produto(s) encontrado(s).", products.size());

        int saved = 0;
        int skipped = 0;

        for (Product product : products) {
            PriceProvider provider = priceProviderFactory.getProvider(product.getProvider());

            if (provider == null) {
                log.info("[PriceFetch] Provider '{}' não suportado. Pulando produto: '{}'.",
                        product.getProvider(), product.getName());
                skipped++;
                continue;
            }

            BigDecimal price = provider.fetchPrice(product.getUrl());

            if (price == null) {
                log.warn("[PriceFetch] Não foi possível obter preço para o produto: '{}'.", product.getName());
                skipped++;
                continue;
            }

            PriceHistory history = new PriceHistory();
            history.setProduct(product);
            history.setPrice(price);
            priceHistoryRepository.save(history);

            log.info("[PriceFetch] Preço salvo para '{}': R$ {}", product.getName(), price);
            saved++;
        }

        log.info("[PriceFetch] Concluído. {} preço(s) salvo(s), {} produto(s) pulado(s).", saved, skipped);
    }
}
