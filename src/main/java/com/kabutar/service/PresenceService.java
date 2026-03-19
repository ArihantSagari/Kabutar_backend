package com.kabutar.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PresenceService {

    private final Map<String, Long> lastSeen = new ConcurrentHashMap<>();

    private static final long TIMEOUT = 10000; // 10 seconds

    public void heartbeat(String user){
        lastSeen.put(user, System.currentTimeMillis());
    }

    public List<String> findOfflineUsers(){
        long now = System.currentTimeMillis();

        List<String> offline = new ArrayList<>();

        for(Map.Entry<String, Long> entry : lastSeen.entrySet()){
            if(now - entry.getValue() > TIMEOUT){
                offline.add(entry.getKey());
            }
        }

        return offline;
    }

    public void removeUser(String user){
        lastSeen.remove(user);
    }
}