package com.kabutar.service;

import com.kabutar.dto.AuthResponse;
import com.kabutar.dto.LoginRequest;
import com.kabutar.dto.RegisterRequest;
import com.kabutar.entity.DeviceSession;
import com.kabutar.entity.User;
import com.kabutar.repository.UserRepository;
import com.kabutar.security.JwtService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Authentication service containing core
 * authentication business logic.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final LoginSecurityService loginSecurityService;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       RefreshTokenService refreshTokenService,
                       LoginSecurityService loginSecurityService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.loginSecurityService = loginSecurityService;
    }

    /**
     * Register new user
     */
    public void register(RegisterRequest request) {

        if (userRepository.existsByUserId(request.getUserId())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setUserId(request.getUserId());
        user.setDisplayName(request.getDisplayName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    /**
     * Login user
     */
    public AuthResponse login(LoginRequest request,
                              String deviceInfo,
                              String ipAddress) {

        // brute force protection
        loginSecurityService.checkLock(request.getUserId(), ipAddress);

        User user = userRepository
                .findByUserId(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("Invalid credentials"));

        boolean passwordMatch = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatch) {

            loginSecurityService.recordFailedAttempt(
                    request.getUserId(),
                    ipAddress
            );

            throw new RuntimeException("Invalid credentials");
        }

        // reset failed attempts
        loginSecurityService.resetAttempts(
                request.getUserId(),
                ipAddress
        );

        // create device session
        DeviceSession session =
                refreshTokenService.createSession(
                        user,
                        deviceInfo,
                        ipAddress
                );

        String accessToken =
                jwtService.generateToken(user.getUserId());

        return new AuthResponse(
                accessToken,
                session.getRefreshToken()
        );
    }

    /**
     * Refresh access token
     */
    public AuthResponse refreshToken(String refreshToken) {

        DeviceSession session =
                refreshTokenService.validateRefreshToken(refreshToken);

        session = refreshTokenService.rotateRefreshToken(session);

        String accessToken =
                jwtService.generateToken(
                        session.getUser().getUserId()
                );

        return new AuthResponse(
                accessToken,
                session.getRefreshToken()
        );
    }

    /**
     * Logout user
     */
    public void logout(String refreshToken) {

        refreshTokenService.revokeSession(refreshToken);

    }
}