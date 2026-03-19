package com.kabutar.service;

import java.util.UUID;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kabutar.dto.MatchEvent;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class MatchmakingService {

    private final StringRedisTemplate redis;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String QUEUE_KEY = "chat:queue";
    private static final String WAITING_KEY = "chat:waiting";
    private static final String ROOM_KEY = "chat:room:";

    private final DefaultRedisScript<String> script;

    public MatchmakingService(
        StringRedisTemplate redis,
        SimpMessagingTemplate messagingTemplate
    ){

        this.redis = redis;
        this.messagingTemplate = messagingTemplate;

        script = new DefaultRedisScript<>();
        script.setLocation(
            new org.springframework.core.io.ClassPathResource("redis/matchmaking.lua")
        );
        script.setResultType(String.class);
    }


/* =================================================
   ENQUEUE USER
================================================= */

    public String enqueueUser(String userId){

        System.out.println("🎯 Matchmaking request from: " + userId);

        String partner = redis.execute(
            script,
            List.of(QUEUE_KEY, WAITING_KEY),
            userId
        );

        System.out.println("Lua returned partner: " + partner);

        if(partner != null){

            String roomId = UUID.randomUUID().toString();

            System.out.println("✅ Matched " + userId + " with " + partner);



            /* STORE ROOM MEMBERS */

            redis.opsForValue().set(
                ROOM_KEY + userId,
                roomId
            );

            redis.opsForValue().set(
                ROOM_KEY + partner,
                roomId
            );



            /* SEND MATCH EVENT USING USER QUEUE */

            messagingTemplate.convertAndSendToUser(
                userId,
                "/queue/match",
                new MatchEvent(roomId, partner)
            );

            messagingTemplate.convertAndSendToUser(
                partner,
                "/queue/match",
                new MatchEvent(roomId, userId)
            );

            return roomId;
        }

        System.out.println("🕒 User queued: " + userId);

        return null;
    }



/* =================================================
   REMOVE USER FROM QUEUE
================================================= */

    public void removeFromQueue(String userId){

        redis.opsForList().remove(QUEUE_KEY, 1, userId);
        redis.delete(WAITING_KEY + ":" + userId);

        System.out.println("🛑 Removed user from queue: " + userId);
    }

}