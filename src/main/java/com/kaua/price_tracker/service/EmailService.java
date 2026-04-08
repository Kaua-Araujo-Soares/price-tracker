package com.kaua.price_tracker.service;

import com.kaua.price_tracker.model.PriceAlert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendAlertTriggered(PriceAlert alert, BigDecimal currentPrice) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(alert.getEmailToNotify());
            message.setSubject("🔔 Alerta de preço atingido — " + alert.getProduct().getName());
            message.setText(buildEmailBody(alert, currentPrice));

            mailSender.send(message);
            log.info("[Email] Notificação enviada para {} — Alerta {}", alert.getEmailToNotify(), alert.getId());
        } catch (Exception e) {
            log.error("[Email] Falha ao enviar para {} — Alerta {}: {}", alert.getEmailToNotify(), alert.getId(), e.getMessage());
        }
    }

    private String buildEmailBody(PriceAlert alert, BigDecimal currentPrice) {
        return """
                Olá!

                O produto "%s" atingiu o preço alvo!

                Preço alvo:    R$ %s
                Preço atual:   R$ %s
                Disparado em:  %s

                Acesse o produto: %s

                ---
                Price Tracker — Monitoramento automático de preços
                """.formatted(
                alert.getProduct().getName(),
                alert.getTargetPrice(),
                currentPrice,
                alert.getTriggeredAt(),
                alert.getProduct().getUrl()
        );
    }
}
