package com.kabutar.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key signingKey;

    /**
     * Initialize signing key once
     */
    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generate JWT token
     */
    public String generateToken(String userId) {

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract userId
     */
    public String extractUserId(String token) {

        return getClaims(token).getSubject();
    }

    /**
     * Validate token
     */
    public boolean validateToken(String token) {

        try {
            getClaims(token);
            return true;

        } catch (ExpiredJwtException e) {
            System.out.println("❌ JWT expired");
        } catch (UnsupportedJwtException e) {
            System.out.println("❌ Unsupported JWT");
        } catch (MalformedJwtException e) {
            System.out.println("❌ Malformed JWT");
        } catch (SecurityException e) {
            System.out.println("❌ Invalid signature");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Empty JWT claims");
        }

        return false;
    }

    /**
     * Internal helper
     */
    private Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}