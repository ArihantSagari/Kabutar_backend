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

import java.util.List;

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

            List<String> authHeaders = accessor.getNativeHeader("Authorization");

            if (authHeaders == null || authHeaders.isEmpty()) {
                throw new AccessDeniedException("Missing Authorization header");
            }

            String bearer = authHeaders.get(0);

            if (!bearer.startsWith("Bearer ")) {
                throw new AccessDeniedException("Invalid Authorization format");
            }

            String token = bearer.substring(7);

            try {

                // ✔ Validate FIRST
                if (!jwtService.validateToken(token)) {
                    throw new AccessDeniedException("Invalid token");
                }

                String username = jwtService.extractUserId(token);

                // ✔ Set authenticated user
                accessor.setUser(() -> username);

                log.info("WebSocket authenticated user={}", username);

            } catch (Exception e) {
                log.warn("WebSocket authentication failed: {}", e.getMessage());
                throw new AccessDeniedException("Unauthorized WebSocket");
            }
        }

        return message;
    }
}