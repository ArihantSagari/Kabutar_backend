package com.kabutar.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PresenceScheduler {

    private final PresenceService presence;
    private final SimpMessagingTemplate messaging;

    public PresenceScheduler(
            PresenceService presence,
            SimpMessagingTemplate messaging
    ){
        this.presence = presence;
        this.messaging = messaging;
    }

    @Scheduled(fixedRate = 5000)
    public void checkOfflineUsers(){

        var offlineUsers = presence.findOfflineUsers();

        for(String user : offlineUsers){

            messaging.convertAndSend(
                    "/topic/presence",
                    Map.of(
                            "user", user,
                            "status", "OFFLINE",
                            "timestamp", System.currentTimeMillis()
                    )
            );

            presence.removeUser(user);
        }
    }
}