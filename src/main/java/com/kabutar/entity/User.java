package com.kabutar.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User entity represents a registered user in Kabutar.
 * Each instance maps to a row in the "users" table.
 */
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "userId"),
                @UniqueConstraint(columnNames = "email")
        }
)
public class User {

    /**
     * Primary key for the table.
     * UUID is safer than incremental IDs because it
     * prevents ID guessing attacks.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Public username used for login.
     * Must be unique and start with '@'
     */
    @Column(nullable = false, unique = true, length = 30)
    private String userId;

    /**
     * Display name shown in chats.
     */
    @Column(nullable = false, length = 60)
    private String displayName;

    /**
     * User email address.
     * Must be unique.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Hashed password stored using BCrypt.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Indicates whether the user verified email.
     */
    @Column(nullable = false)
    private boolean emailVerified = false;

    /**
     * Account creation timestamp.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor required by JPA
     */
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    /* ===========================
       GETTERS AND SETTERS
       =========================== */

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}