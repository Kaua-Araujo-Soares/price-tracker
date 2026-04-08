package com.kaua.price_tracker.repository;

import com.kaua.price_tracker.model.AlertStatus;
import com.kaua.price_tracker.model.PriceAlert;
import com.kaua.price_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {

    List<PriceAlert> findByProductId(Long productId);

    List<PriceAlert> findByProductIdAndUser(Long productId, User user);

    List<PriceAlert> findByProductIdAndStatus(Long productId, AlertStatus status);

    List<PriceAlert> findByStatus(AlertStatus status);

    Optional<PriceAlert> findByIdAndUser(Long id, User user);
}
