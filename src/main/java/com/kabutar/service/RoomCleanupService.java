package com.kabutar.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomCleanupService {

    /* roomId -> last activity timestamp */
    private final Map<String, Long> roomActivity = new ConcurrentHashMap<>();

    /* 2 minutes inactivity timeout */
    private static final long ROOM_TIMEOUT = 120000;



/* =================================================
 UPDATE ROOM ACTIVITY
================================================= */

    public void updateRoom(String roomId) {

        roomActivity.put(roomId, System.currentTimeMillis());
    }



/* =================================================
 CHECK ROOM ACTIVE
================================================= */

    public boolean isRoomActive(String roomId) {

        Long last = roomActivity.get(roomId);

        if (last == null) return false;

        return (System.currentTimeMillis() - last) < ROOM_TIMEOUT;
    }



/* =================================================
 REMOVE ROOM
================================================= */

    public void removeRoom(String roomId) {

        roomActivity.remove(roomId);
    }



/* =================================================
 CLEANUP INACTIVE ROOMS
================================================= */

    public void cleanupRooms() {

        long now = System.currentTimeMillis();

        roomActivity.entrySet().removeIf(entry ->
                (now - entry.getValue()) > ROOM_TIMEOUT
        );
    }
}