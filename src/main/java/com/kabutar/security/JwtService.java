package com.kabutar.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Handles JWT token generation and validation.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Create signing key
     */
    private Key getSigningKey() {

        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generate JWT access token
     */
    public String generateToken(String userId) {

        return Jwts.builder()

                .setSubject(userId)

                .setIssuedAt(new Date())

                .setExpiration(new Date(System.currentTimeMillis() + expiration))

                .signWith(getSigningKey(), SignatureAlgorithm.HS256)

                .compact();
    }

    /**
     * Extract userId from token
     */
    public String extractUserId(String token) {

        return Jwts.parserBuilder()

                .setSigningKey(getSigningKey())

                .build()

                .parseClaimsJws(token)

                .getBody()

                .getSubject();
    }

    /**
     * Validate token
     */
    public boolean validateToken(String token) {

        try {

            Jwts.parserBuilder()

                    .setSigningKey(getSigningKey())

                    .build()

                    .parseClaimsJws(token);

            return true;

        } catch (Exception e) {

            return false;

        }
    }
}