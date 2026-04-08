package com.kaua.price_tracker.controller;

import com.kaua.price_tracker.dto.PriceRequestDTO;
import com.kaua.price_tracker.dto.PriceResponseDTO;
import com.kaua.price_tracker.service.PriceHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/prices")
@RequiredArgsConstructor
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    @PostMapping
    public ResponseEntity<PriceResponseDTO> registerPrice(
            @PathVariable Long productId,
            @Valid @RequestBody PriceRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(priceHistoryService.registerPrice(productId, dto));
    }

    @GetMapping
    public ResponseEntity<List<PriceResponseDTO>> findByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(priceHistoryService.findByProductId(productId));
    }
}
