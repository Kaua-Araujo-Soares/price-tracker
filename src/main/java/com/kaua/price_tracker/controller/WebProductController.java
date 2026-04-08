package com.kaua.price_tracker.controller;

import com.kaua.price_tracker.dto.ProductRequestDTO;
import com.kaua.price_tracker.dto.ProductResponseDTO;
import com.kaua.price_tracker.model.Provider;
import com.kaua.price_tracker.service.PriceAlertService;
import com.kaua.price_tracker.service.PriceHistoryService;
import com.kaua.price_tracker.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class WebProductController {

    private final ProductService productService;
    private final PriceHistoryService priceHistoryService;
    private final PriceAlertService priceAlertService;

    @GetMapping
    public String list(Model model) {
        List<ProductResponseDTO> products = productService.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("dto", new ProductRequestDTO());
        model.addAttribute("providers", Provider.values());
        return "product-new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute ProductRequestDTO dto, Model model) {
        try {
            productService.create(dto);
            return "redirect:/products";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("dto", dto);
            model.addAttribute("providers", Provider.values());
            return "product-new";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ProductResponseDTO product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("prices", priceHistoryService.findByProductId(id));
        model.addAttribute("alerts", priceAlertService.findByProductId(id));
        return "product-detail";
    }
}
