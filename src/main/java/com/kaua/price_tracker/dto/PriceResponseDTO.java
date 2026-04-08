package com.kaua.price_tracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PriceResponseDTO {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private LocalDateTime recordedAt;
}
