package com.kabutar.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisMatchSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;

    public RedisMatchSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        String msg = new String(message.getBody());

        String[] parts = msg.split(":");

        String userId = parts[0];
        String roomId = parts[1];

        messagingTemplate.convertAndSend(
                "/topic/match/" + userId,
                roomId
        );
    }
}