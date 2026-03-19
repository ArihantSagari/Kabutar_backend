package com.kabutar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO used when a user registers an account.
 *
 * This class represents the JSON body received from the frontend.
 */
public class RegisterRequest {

    /**
     * User login ID
     * Must start with '@'
     */
    @NotBlank(message = "User ID is required")
    @Pattern(
        regexp = "^@[a-zA-Z0-9_]{4,20}$",
        message = "User ID must start with @ and be 5-21 characters"
    )
    private String userId;

    /**
     * Display name shown to other users
     */
    @NotBlank(message = "Display name is required")
    @Size(min = 2, max = 50)
    private String displayName;

    /**
     * User email
     */
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    /**
     * Password entered by user
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    /* ===== GETTERS AND SETTERS ===== */

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
}