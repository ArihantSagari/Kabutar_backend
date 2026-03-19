package com.kabutar.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomServiceImpl implements RoomService {

    // roomId -> users
    private final Map<String, Set<String>> rooms = new ConcurrentHashMap<>();

    /* =================================================
       CHECK USER IN ROOM
    ================================================= */

    @Override
    public boolean isUserInRoom(String roomId, String username) {
        return rooms.getOrDefault(roomId, Set.of()).contains(username);
    }

    /* =================================================
       ADD USER
    ================================================= */

    public void addUserToRoom(String roomId, String username) {
        rooms.computeIfAbsent(roomId, r -> ConcurrentHashMap.newKeySet())
             .add(username);
    }

    /* =================================================
       REMOVE USER
    ================================================= */

    public void removeUserFromRoom(String roomId, String username) {
        Set<String> users = rooms.get(roomId);
        if (users != null) {
            users.remove(username);
            if (users.isEmpty()) {
                rooms.remove(roomId);
            }
        }
    }

    /* =================================================
       OPTIONAL: GET USERS
    ================================================= */

    public Set<String> getUsers(String roomId) {
        return rooms.getOrDefault(roomId, Set.of());
    }
}