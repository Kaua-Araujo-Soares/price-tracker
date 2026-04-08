package com.kaua.price_tracker.controller;

import com.kaua.price_tracker.dto.RegisterRequestDTO;
import com.kaua.price_tracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "Email ou senha inválidos.");
        if (logout != null) model.addAttribute("message", "Você saiu com sucesso.");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("dto", new RegisterRequestDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequestDTO dto, Model model) {
        try {
            authService.register(dto);
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("dto", dto);
            return "register";
        }
    }
}
