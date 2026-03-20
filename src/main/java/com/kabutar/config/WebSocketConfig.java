package com.kabutar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthInterceptor authInterceptor;

    public WebSocketConfig(WebSocketAuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    /**
     * Message broker configuration
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        // ✅ For pub-sub messaging
        config.enableSimpleBroker("/topic", "/queue");

        // ✅ Prefix for sending messages from client
        config.setApplicationDestinationPrefixes("/app");

        // ✅ Required for user-specific messaging
        config.setUserDestinationPrefix("/user");
    }

    /**
     * WebSocket endpoint configuration
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry
            .addEndpoint("/ws")

            // ✅ IMPORTANT: allow frontend domains (NOT "*")
            .setAllowedOriginPatterns(
                "http://localhost:3000",
                "https://*.vercel.app",
                "https://*.ngrok-free.dev"
            )

            // ✅ REQUIRED for SockJS (your frontend uses it)
            .withSockJS();
    }

    /**
     * Interceptor for authentication (JWT)
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authInterceptor);
    }
}