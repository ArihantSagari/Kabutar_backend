package com.kabutar.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.kabutar.dto.MatchResponse;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class ChatMatchService {

    private final Queue<String> waitingUsers = new ConcurrentLinkedQueue<>();
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMatchService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public synchronized void findMatch(String userId) {

        String partner = waitingUsers.poll();

        if (partner != null && !partner.equals(userId)) {

            String roomId = UUID.randomUUID().toString();

            System.out.println("✅ Matched " + userId + " with " + partner);

            /* SEND MATCH EVENT TO BOTH USERS */

            messagingTemplate.convertAndSendToUser(
                    userId,
                    "/queue/match",
                    new MatchResponse(roomId)
            );

            messagingTemplate.convertAndSendToUser(
                    partner,
                    "/queue/match",
                    new MatchResponse(roomId)
            );

        } else {

            System.out.println("🕒 User queued: " + userId);

            waitingUsers.add(userId);
        }
    }
}