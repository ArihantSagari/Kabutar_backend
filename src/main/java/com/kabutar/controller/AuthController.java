package com.kabutar.controller;

import com.kabutar.dto.AuthResponse;
import com.kabutar.dto.LoginRequest;
import com.kabutar.dto.RegisterRequest;
import com.kabutar.repository.UserRepository;
import com.kabutar.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Authentication REST controller
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService,
                          UserRepository userRepository) {

        this.authService = authService;
        this.userRepository = userRepository;
    }

    /**
     * Register user
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully"));
    }

    /**
     * Login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        String deviceInfo = httpRequest.getHeader("User-Agent");
        String ipAddress = httpRequest.getRemoteAddr();

        AuthResponse response =
                authService.login(request, deviceInfo, ipAddress);

        return ResponseEntity.ok(response);
    }

    /**
     * Refresh token endpoint
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody Map<String, String> request) {

        AuthResponse response =
                authService.refreshToken(request.get("refreshToken"));

        return ResponseEntity.ok(response);
    }

    /**
     * Logout endpoint
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestBody Map<String, String> request) {

        authService.logout(request.get("refreshToken"));

        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * Username availability check
     */
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(
            @RequestParam String userId) {

        boolean available =
                !userRepository.existsByUserId(userId);

        return ResponseEntity.ok(
                Map.of("available", available)
        );
    }

    /**
     * Email availability check
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(
            @RequestParam String email) {

        boolean available =
                !userRepository.existsByEmail(email);

        return ResponseEntity.ok(
                Map.of("available", available)
        );
    }
}