package com.kaua.price_tracker.dto;

import com.kaua.price_tracker.model.AlertStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AlertResponseDTO {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal targetPrice;
    private String emailToNotify;
    private AlertStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime triggeredAt;
}
