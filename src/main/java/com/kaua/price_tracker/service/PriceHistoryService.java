package com.kaua.price_tracker.service;

import com.kaua.price_tracker.dto.PriceRequestDTO;
import com.kaua.price_tracker.dto.PriceResponseDTO;
import com.kaua.price_tracker.exception.ResourceNotFoundException;
import com.kaua.price_tracker.model.PriceHistory;
import com.kaua.price_tracker.model.Product;
import com.kaua.price_tracker.repository.PriceHistoryRepository;
import com.kaua.price_tracker.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;
    private final ProductRepository productRepository;

    public PriceResponseDTO registerPrice(Long productId, PriceRequestDTO dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + productId));

        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setProduct(product);
        priceHistory.setPrice(dto.getPrice());

        PriceHistory saved = priceHistoryRepository.save(priceHistory);
        return toResponse(saved);
    }

    public List<PriceResponseDTO> findByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Produto não encontrado: " + productId);
        }

        return priceHistoryRepository.findByProductId(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private PriceResponseDTO toResponse(PriceHistory priceHistory) {
        PriceResponseDTO dto = new PriceResponseDTO();
        dto.setId(priceHistory.getId());
        dto.setProductId(priceHistory.getProduct().getId());
        dto.setProductName(priceHistory.getProduct().getName());
        dto.setPrice(priceHistory.getPrice());
        dto.setRecordedAt(priceHistory.getRecordedAt());
        return dto;
    }
}
