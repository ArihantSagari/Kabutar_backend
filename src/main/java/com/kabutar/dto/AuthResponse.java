package com.kabutar.dto;

public class AuthResponse {

    private String Token;
    private String refreshToken;

    public AuthResponse(String accessToken, String refreshToken) {
        this.Token = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return Token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}