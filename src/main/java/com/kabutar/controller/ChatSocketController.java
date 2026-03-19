package com.kabutar.controller;

import com.kabutar.dto.IncomingMessage;
import com.kabutar.dto.MessageStatusDTO;
import com.kabutar.model.ChatEvent;
import com.kabutar.model.ChatEventType;
import com.kabutar.service.PresenceService;
import com.kabutar.service.RoomCleanupService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Controller
@EnableScheduling
public class ChatSocketController {

    private static final Logger log =
            LoggerFactory.getLogger(ChatSocketController.class);

    private static final int MAX_MESSAGE_LENGTH = 2000;

    private final SimpMessagingTemplate messaging;
    private final PresenceService presence;
    private final RoomCleanupService cleanup;

    public ChatSocketController(
            SimpMessagingTemplate messaging,
            PresenceService presence,
            RoomCleanupService cleanup
    ){
        this.messaging = messaging;
        this.presence = presence;
        this.cleanup = cleanup;
    }

    /* =================================================
       MESSAGE
    ================================================= */

    @MessageMapping("/chat/{roomId}")
    public void message(
            @DestinationVariable String roomId,
            @Payload IncomingMessage incoming,
            Principal principal
    ){
        if(principal == null) return;

        String sender = principal.getName();
        String content = incoming.getContent();

        if(content == null || content.trim().isEmpty()) return;

        if(content.length() > MAX_MESSAGE_LENGTH){
            log.warn("Message too long | user={}", sender);
            return;
        }

        ChatEvent event = new ChatEvent();

        event.setId(
                incoming.getId() != null
                        ? incoming.getId()
                        : UUID.randomUUID().toString()
        );

        event.setType(ChatEventType.MESSAGE);
        event.setRoomId(roomId);
        event.setSender(sender);
        event.setContent(content.trim());
        event.setTimestamp(System.currentTimeMillis());

        broadcast(roomId, event);
    }

    /* =================================================
       TYPING
    ================================================= */

    @MessageMapping("/chat/{roomId}/typing")
    public void typing(
            @DestinationVariable String roomId,
            Principal principal
    ){
        if(principal == null) return;

        broadcast(roomId, buildEvent(roomId, principal, ChatEventType.TYPING));
    }

    @MessageMapping("/chat/{roomId}/stop-typing")
    public void stopTyping(
            @DestinationVariable String roomId,
            Principal principal
    ){
        if(principal == null) return;

        broadcast(roomId, buildEvent(roomId, principal, ChatEventType.STOP_TYPING));
    }

    /* =================================================
       DELIVERY
    ================================================= */

    @MessageMapping("/chat/{roomId}/delivered")
    public void delivered(
            @DestinationVariable String roomId,
            @Payload MessageStatusDTO incoming,
            Principal principal
    ){
        if(principal == null) return;
        if(incoming.getMessageId() == null) return;

        ChatEvent event = buildEvent(roomId, principal, ChatEventType.DELIVERED);
        event.setMessageId(incoming.getMessageId());

        broadcast(roomId, event);
    }

    @MessageMapping("/chat/{roomId}/seen")
    public void seen(
            @DestinationVariable String roomId,
            @Payload MessageStatusDTO incoming,
            Principal principal
    ){
        if(principal == null) return;
        if(incoming.getMessageId() == null) return;

        ChatEvent event = buildEvent(roomId, principal, ChatEventType.SEEN);
        event.setMessageId(incoming.getMessageId());

        broadcast(roomId, event);
    }

    /* =================================================
       PRESENCE
    ================================================= */

    @MessageMapping("/presence/heartbeat")
    public void heartbeat(Principal principal){
        if(principal == null) return;

        String username = principal.getName();

        presence.heartbeat(username);

        messaging.convertAndSend(
                "/topic/presence",
                Map.of(
                        "user", username,
                        "status", "ONLINE",
                        "timestamp", System.currentTimeMillis()
                )
        );
    }

    /* =================================================
       ROOM ACTIONS
    ================================================= */

    @MessageMapping("/chat/{roomId}/skip")
    public void skip(
            @DestinationVariable String roomId,
            Principal principal
    ){
        if(principal == null) return;

        broadcast(roomId, buildEvent(roomId, principal, ChatEventType.SKIP));
    }

    @MessageMapping("/chat/{roomId}/end")
    public void end(
            @DestinationVariable String roomId,
            Principal principal
    ){
        if(principal == null) return;

        broadcast(roomId, buildEvent(roomId, principal, ChatEventType.END));
    }

    /* =================================================
       HELPERS
    ================================================= */

    private ChatEvent buildEvent(
            String roomId,
            Principal principal,
            ChatEventType type
    ){
        ChatEvent event = new ChatEvent();
        event.setId(UUID.randomUUID().toString());
        event.setType(type);
        event.setRoomId(roomId);
        event.setSender(principal.getName());
        event.setTimestamp(System.currentTimeMillis());
        return event;
    }

    private void broadcast(String roomId, ChatEvent event){
        cleanup.updateRoom(roomId);

        messaging.convertAndSend(
                "/topic/chat/" + roomId,
                event
        );
    }

    /* =================================================
       ERROR HANDLER (VERY IMPORTANT)
    ================================================= */

    @MessageExceptionHandler
    public void handleException(Throwable e) {
        log.error("🔥 WebSocket Error", e);
    }
}