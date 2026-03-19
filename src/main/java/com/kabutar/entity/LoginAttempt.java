package com.kabutar.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Tracks login attempts for security purposes.
 *
 * Used to prevent brute-force attacks.
 */
@Entity
@Table(name = "login_attempts")
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Username attempted
     */
    @Column(nullable = false)
    private String userId;

    /**
     * IP address of the attacker
     */
    @Column(nullable = false)
    private String ipAddress;

    /**
     * Number of failed attempts
     */
    private int attempts;

    /**
     * Last attempt timestamp
     */
    private LocalDateTime lastAttempt;

    /**
     * Lock expiration time
     */
    private LocalDateTime lockedUntil;

    public LoginAttempt() {}

    /* GETTERS AND SETTERS */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public LocalDateTime getLastAttempt() {
        return lastAttempt;
    }

    public void setLastAttempt(LocalDateTime lastAttempt) {
        this.lastAttempt = lastAttempt;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
}