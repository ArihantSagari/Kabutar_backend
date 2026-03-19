package com.kabutar.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DeviceSession represents a login session for a specific device.
 *
 * This allows us to:
 * - track active sessions
 * - implement refresh tokens
 * - enforce single-device login
 */
@Entity
@Table(name = "device_sessions")
public class DeviceSession {

    /**
     * Primary key for session
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * User owning this session
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Refresh token stored securely
     */
    @Column(nullable = false, unique = true)
    private String refreshToken;

    /**
     * Device information
     * Example: Chrome / Windows
     */
    @Column(nullable = false)
    private String deviceInfo;

    /**
     * User IP address
     */
    @Column(nullable = false)
    private String ipAddress;

    /**
     * Session creation time
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Session expiration time
     */
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Indicates if session is revoked (logout)
     */
    private boolean revoked = false;

    public DeviceSession() {}

    /* ================= GETTERS / SETTERS ================= */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}