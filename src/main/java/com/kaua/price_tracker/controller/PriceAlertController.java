package com.kaua.price_tracker.controller;

import com.kaua.price_tracker.dto.AlertCheckRequestDTO;
import com.kaua.price_tracker.dto.AlertRequestDTO;
import com.kaua.price_tracker.dto.AlertResponseDTO;
import com.kaua.price_tracker.service.PriceAlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PriceAlertController {

    private final PriceAlertService priceAlertService;

    @PostMapping("/api/products/{productId}/alerts")
    public ResponseEntity<AlertResponseDTO> create(
            @PathVariable Long productId,
            @Valid @RequestBody AlertRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(priceAlertService.create(productId, dto));
    }

    @GetMapping("/api/products/{productId}/alerts")
    public ResponseEntity<List<AlertResponseDTO>> findByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(priceAlertService.findByProductId(productId));
    }

    @PatchMapping("/api/alerts/{alertId}/disable")
    public ResponseEntity<AlertResponseDTO> disable(@PathVariable Long alertId) {
        return ResponseEntity.ok(priceAlertService.disable(alertId));
    }

    @PostMapping("/api/alerts/check")
    public ResponseEntity<List<AlertResponseDTO>> checkAlerts(@Valid @RequestBody AlertCheckRequestDTO dto) {
        return ResponseEntity.ok(priceAlertService.checkAlerts(dto));
    }
}
