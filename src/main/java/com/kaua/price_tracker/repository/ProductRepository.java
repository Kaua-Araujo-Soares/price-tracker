package com.kaua.price_tracker.repository;

import com.kaua.price_tracker.model.Product;
import com.kaua.price_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByUser(User user);

    Optional<Product> findByIdAndUser(Long id, User user);

    boolean existsByIdAndUser(Long id, User user);
}