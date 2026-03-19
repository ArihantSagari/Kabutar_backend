package com.kabutar.socket;

import com.kabutar.service.PresenceService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
public class SocketDisconnectListener {

    private final PresenceService presence;
    private final SimpMessagingTemplate messaging;

    public SocketDisconnectListener(
            PresenceService presence,
            SimpMessagingTemplate messaging
    ){
        this.presence = presence;
        this.messaging = messaging;
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event){

        SimpMessageHeaderAccessor accessor =
                SimpMessageHeaderAccessor.wrap(event.getMessage());

        String username = (String) accessor.getSessionAttributes().get("username");

        if(username == null) return;

        presence.removeUser(username);

        messaging.convertAndSend(
                "/topic/presence",
                Map.of(
                        "user", username,
                        "status", "OFFLINE",
                        "timestamp", System.currentTimeMillis()
                )
        );
    }
}