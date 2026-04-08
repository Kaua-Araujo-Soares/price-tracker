package com.kaua.price_tracker.dto;

import com.kaua.price_tracker.model.Provider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "URL é obrigatória")
    private String url;

    @NotNull(message = "Provider é obrigatório")
    private Provider provider;
}