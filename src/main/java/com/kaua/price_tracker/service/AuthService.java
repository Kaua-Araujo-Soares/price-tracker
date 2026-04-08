package com.kaua.price_tracker.service;

import com.kaua.price_tracker.dto.AuthResponseDTO;
import com.kaua.price_tracker.dto.LoginRequestDTO;
import com.kaua.price_tracker.dto.RegisterRequestDTO;
import com.kaua.price_tracker.model.User;
import com.kaua.price_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso: " + dto.getEmail());
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponseDTO(token, user.getEmail(), user.getName());
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        String token = jwtService.generateToken(user);
        return new AuthResponseDTO(token, user.getEmail(), user.getName());
    }
}
