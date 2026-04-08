package com.kaua.price_tracker.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AlertCheckRequestDTO {

    @NotNull(message = "productId é obrigatório")
    private Long productId;

    @NotNull(message = "Preço atual é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço atual deve ser maior que zero")
    private BigDecimal currentPrice;
}
