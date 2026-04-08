package com.kaua.price_tracker.scheduler;

import com.kaua.price_tracker.model.AlertStatus;
import com.kaua.price_tracker.model.PriceAlert;
import com.kaua.price_tracker.model.PriceHistory;
import com.kaua.price_tracker.repository.PriceAlertRepository;
import com.kaua.price_tracker.repository.PriceHistoryRepository;
import com.kaua.price_tracker.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceAlertScheduler {

    private final PriceAlertRepository priceAlertRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final EmailService emailService;

    @Scheduled(fixedDelayString = "${app.scheduler.price-check-interval}")
    public void checkAlerts() {
        log.info("[Scheduler] Iniciando verificação de alertas...");

        List<PriceAlert> activeAlerts = priceAlertRepository.findByStatus(AlertStatus.ACTIVE);
        log.info("[Scheduler] {} alerta(s) ACTIVE encontrado(s).", activeAlerts.size());

        int triggered = 0;

        for (PriceAlert alert : activeAlerts) {
            Long productId = alert.getProduct().getId();

            Optional<PriceHistory> latestPrice =
                    priceHistoryRepository.findTopByProductIdOrderByRecordedAtDesc(productId);

            if (latestPrice.isEmpty()) {
                log.info("[Scheduler] Produto {} sem preço registrado. Pulando.", productId);
                continue;
            }

            BigDecimal currentPrice = latestPrice.get().getPrice();

            if (currentPrice.compareTo(alert.getTargetPrice()) <= 0) {
                alert.setStatus(AlertStatus.TRIGGERED);
                alert.setTriggeredAt(LocalDateTime.now());
                priceAlertRepository.save(alert);

                log.info(
                    "[Scheduler] Alerta {} DISPARADO! Produto: '{}' | Preço atual: {} | Alvo: {}",
                    alert.getId(),
                    alert.getProduct().getName(),
                    currentPrice,
                    alert.getTargetPrice()
                );

                emailService.sendAlertTriggered(alert, currentPrice);
                triggered++;
            } else {
                log.info(
                    "[Scheduler] Alerta {} ainda não atingido. Preço atual: {} | Alvo: {}",
                    alert.getId(),
                    currentPrice,
                    alert.getTargetPrice()
                );
            }
        }

        log.info("[Scheduler] Verificação concluída. {} alerta(s) disparado(s).", triggered);
    }
}
