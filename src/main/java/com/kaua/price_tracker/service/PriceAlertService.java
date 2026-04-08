package com.kaua.price_tracker.service;

import com.kaua.price_tracker.dto.AlertCheckRequestDTO;
import com.kaua.price_tracker.dto.AlertRequestDTO;
import com.kaua.price_tracker.dto.AlertResponseDTO;
import com.kaua.price_tracker.exception.ResourceNotFoundException;
import com.kaua.price_tracker.model.AlertStatus;
import com.kaua.price_tracker.model.PriceAlert;
import com.kaua.price_tracker.model.Product;
import com.kaua.price_tracker.model.User;
import com.kaua.price_tracker.repository.PriceAlertRepository;
import com.kaua.price_tracker.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceAlertService {

    private final PriceAlertRepository priceAlertRepository;
    private final ProductRepository productRepository;

    public AlertResponseDTO create(Long productId, AlertRequestDTO dto) {
        User user = getAuthenticatedUser();

        Product product = productRepository.findByIdAndUser(productId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + productId));

        PriceAlert alert = new PriceAlert();
        alert.setProduct(product);
        alert.setUser(user);
        alert.setTargetPrice(dto.getTargetPrice());
        alert.setEmailToNotify(dto.getEmailToNotify());
        alert.setStatus(AlertStatus.ACTIVE);

        return toResponse(priceAlertRepository.save(alert));
    }

    public List<AlertResponseDTO> findByProductId(Long productId) {
        User user = getAuthenticatedUser();

        if (!productRepository.existsByIdAndUser(productId, user)) {
            throw new ResourceNotFoundException("Produto não encontrado: " + productId);
        }

        return priceAlertRepository.findByProductIdAndUser(productId, user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AlertResponseDTO disable(Long alertId) {
        User user = getAuthenticatedUser();

        PriceAlert alert = priceAlertRepository.findByIdAndUser(alertId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado: " + alertId));

        alert.setStatus(AlertStatus.DISABLED);

        return toResponse(priceAlertRepository.save(alert));
    }

    public List<AlertResponseDTO> checkAlerts(AlertCheckRequestDTO dto) {
        if (!productRepository.existsById(dto.getProductId())) {
            throw new ResourceNotFoundException("Produto não encontrado: " + dto.getProductId());
        }

        List<PriceAlert> activeAlerts = priceAlertRepository
                .findByProductIdAndStatus(dto.getProductId(), AlertStatus.ACTIVE);

        List<PriceAlert> triggered = activeAlerts.stream()
                .filter(alert -> dto.getCurrentPrice().compareTo(alert.getTargetPrice()) <= 0)
                .peek(alert -> {
                    alert.setStatus(AlertStatus.TRIGGERED);
                    alert.setTriggeredAt(LocalDateTime.now());
                })
                .toList();

        priceAlertRepository.saveAll(triggered);

        return triggered.stream()
                .map(this::toResponse)
                .toList();
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private AlertResponseDTO toResponse(PriceAlert alert) {
        AlertResponseDTO dto = new AlertResponseDTO();
        dto.setId(alert.getId());
        dto.setProductId(alert.getProduct().getId());
        dto.setProductName(alert.getProduct().getName());
        dto.setTargetPrice(alert.getTargetPrice());
        dto.setEmailToNotify(alert.getEmailToNotify());
        dto.setStatus(alert.getStatus());
        dto.setCreatedAt(alert.getCreatedAt());
        dto.setTriggeredAt(alert.getTriggeredAt());
        return dto;
    }
}
