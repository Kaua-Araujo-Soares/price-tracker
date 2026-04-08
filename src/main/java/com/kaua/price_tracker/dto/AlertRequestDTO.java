package com.kaua.price_tracker.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AlertRequestDTO {

    @NotNull(message = "Preço alvo é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço alvo deve ser maior que zero")
    private BigDecimal targetPrice;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String emailToNotify;
}
