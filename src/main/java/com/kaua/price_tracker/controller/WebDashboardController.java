package com.kaua.price_tracker.controller;

import com.kaua.price_tracker.dto.AlertResponseDTO;
import com.kaua.price_tracker.dto.ProductResponseDTO;
import com.kaua.price_tracker.model.AlertStatus;
import com.kaua.price_tracker.service.PriceAlertService;
import com.kaua.price_tracker.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class WebDashboardController {

    private final ProductService productService;
    private final PriceAlertService priceAlertService;

    @GetMapping
    public String dashboard(Model model) {
        List<ProductResponseDTO> products = productService.findAll();

        List<AlertResponseDTO> allAlerts = products.stream()
                .flatMap(p -> priceAlertService.findByProductId(p.getId()).stream())
                .toList();

        long activeCount = allAlerts.stream()
                .filter(a -> a.getStatus() == AlertStatus.ACTIVE).count();

        long triggeredCount = allAlerts.stream()
                .filter(a -> a.getStatus() == AlertStatus.TRIGGERED).count();

        List<AlertResponseDTO> recentTriggered = allAlerts.stream()
                .filter(a -> a.getStatus() == AlertStatus.TRIGGERED)
                .sorted((a, b) -> b.getTriggeredAt().compareTo(a.getTriggeredAt()))
                .limit(5)
                .toList();

        model.addAttribute("totalProducts", products.size());
        model.addAttribute("activeAlerts", activeCount);
        model.addAttribute("triggeredAlerts", triggeredCount);
        model.addAttribute("recentTriggered", recentTriggered);
        model.addAttribute("products", products);

        return "dashboard";
    }
}
