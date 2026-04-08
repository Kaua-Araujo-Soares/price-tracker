package com.kaua.price_tracker.controller;

import com.kaua.price_tracker.dto.AlertRequestDTO;
import com.kaua.price_tracker.dto.AlertResponseDTO;
import com.kaua.price_tracker.model.AlertStatus;
import com.kaua.price_tracker.service.PriceAlertService;
import com.kaua.price_tracker.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class WebAlertController {

    private final PriceAlertService priceAlertService;
    private final ProductService productService;

    @GetMapping
    public String list(@RequestParam(required = false) String status, Model model) {
        List<AlertResponseDTO> allAlerts = productService.findAll().stream()
                .flatMap(p -> priceAlertService.findByProductId(p.getId()).stream())
                .collect(Collectors.toList());

        List<AlertResponseDTO> filtered = allAlerts;
        if (status != null && !status.isBlank()) {
            AlertStatus filterStatus = AlertStatus.valueOf(status.toUpperCase());
            filtered = allAlerts.stream()
                    .filter(a -> a.getStatus() == filterStatus)
                    .toList();
        }

        model.addAttribute("alerts", filtered);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", AlertStatus.values());
        return "alerts";
    }

    @PostMapping("/{alertId}/disable")
    public String disable(@PathVariable Long alertId) {
        priceAlertService.disable(alertId);
        return "redirect:/alerts";
    }

    @PostMapping("/products/{productId}/new")
    public String create(@PathVariable Long productId,
                         @ModelAttribute AlertRequestDTO dto) {
        priceAlertService.create(productId, dto);
        return "redirect:/products/" + productId;
    }
}
