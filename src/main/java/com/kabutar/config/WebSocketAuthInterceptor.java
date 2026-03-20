package com.kabutar.config;

import com.kabutar.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.*;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private static final Logger log =
            LoggerFactory.getLogger(WebSocketAuthInterceptor.class);

    private final JwtService jwtService;

    public WebSocketAuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String token = null;

            // ✅ 1. Try header (optional)
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            // ✅ 2. Fallback → query param (MAIN FIX)
            if (token == null) {

                String query = accessor.getSessionAttributes() != null
                        ? accessor.getSessionAttributes().toString()
                        : "";

                if (query.contains("token=")) {
                    token = query.split("token=")[1].split("[&}]")[0];
                }
            }

            // ❌ Still no token
            if (token == null) {
                throw new AccessDeniedException("Missing token");
            }

            try {

                if (!jwtService.validateToken(token)) {
                    throw new AccessDeniedException("Invalid token");
                }

                String userId = jwtService.extractUserId(token);

                accessor.setUser(() -> userId);

                log.info("✅ WebSocket authenticated user={}", userId);

            } catch (Exception e) {
                log.warn("❌ WebSocket authentication failed: {}", e.getMessage());
                throw new AccessDeniedException("Unauthorized WebSocket");
            }
        }

        return message;
    }
}