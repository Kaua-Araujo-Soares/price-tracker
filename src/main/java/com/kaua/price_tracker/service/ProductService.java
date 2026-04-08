package com.kaua.price_tracker.service;

import com.kaua.price_tracker.dto.ProductRequestDTO;
import com.kaua.price_tracker.dto.ProductResponseDTO;
import com.kaua.price_tracker.exception.ResourceNotFoundException;
import com.kaua.price_tracker.model.Product;
import com.kaua.price_tracker.model.User;
import com.kaua.price_tracker.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDTO create(ProductRequestDTO dto) {
        User user = getAuthenticatedUser();

        Product product = new Product();
        product.setName(dto.getName());
        product.setUrl(dto.getUrl());
        product.setProvider(dto.getProvider());
        product.setUser(user);

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    public List<ProductResponseDTO> findAll() {
        User user = getAuthenticatedUser();
        return productRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponseDTO findById(Long id) {
        User user = getAuthenticatedUser();
        Product product = productRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + id));
        return toResponse(product);
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private ProductResponseDTO toResponse(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setUrl(product.getUrl());
        dto.setProvider(product.getProvider());
        dto.setCreatedAt(product.getCreatedAt());
        return dto;
    }
}
