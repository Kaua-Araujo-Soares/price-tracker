package com.kaua.price_tracker.dto;

import com.kaua.price_tracker.model.Provider;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String url;
    private Provider provider;
    private LocalDateTime createdAt;
}