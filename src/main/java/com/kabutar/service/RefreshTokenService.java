package com.kabutar.service;

import com.kabutar.entity.DeviceSession;
import com.kabutar.entity.User;
import com.kabutar.repository.DeviceSessionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Handles device sessions and refresh tokens.
 *
 * Security features:
 * - Single device login
 * - Refresh token rotation
 * - Session revocation
 */
@Service
public class RefreshTokenService {

    private final DeviceSessionRepository sessionRepository;

    public RefreshTokenService(DeviceSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * Create login session
     */
    @Transactional
    public DeviceSession createSession(User user,
                                       String deviceInfo,
                                       String ipAddress) {

        /*
         Remove existing sessions so that the same
         account cannot be used simultaneously on
         multiple devices.
        */
        sessionRepository.deleteByUser(user);

        DeviceSession session = new DeviceSession();

        session.setUser(user);
        session.setRefreshToken(UUID.randomUUID().toString());
        session.setDeviceInfo(deviceInfo);
        session.setIpAddress(ipAddress);
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusDays(7));
        session.setRevoked(false);

        return sessionRepository.save(session);
    }

    /**
     * Validate refresh token
     */
    public DeviceSession validateRefreshToken(String token) {

        DeviceSession session = sessionRepository
                .findByRefreshToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (session.isRevoked()) {
            throw new RuntimeException("Session revoked");
        }

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Session expired");
        }

        return session;
    }

    /**
     * Rotate refresh token
     */
    @Transactional
    public DeviceSession rotateRefreshToken(DeviceSession session) {

        session.setRefreshToken(UUID.randomUUID().toString());

        return sessionRepository.save(session);
    }

    /**
     * Logout user
     */
    @Transactional
    public void revokeSession(String refreshToken) {

        DeviceSession session = sessionRepository
                .findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setRevoked(true);

        sessionRepository.save(session);
    }
}