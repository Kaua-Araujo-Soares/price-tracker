package com.kaua.price_tracker.repository;

import com.kaua.price_tracker.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    List<PriceHistory> findByProductId(Long productId);

    Optional<PriceHistory> findTopByProductIdOrderByRecordedAtDesc(Long productId);
}
