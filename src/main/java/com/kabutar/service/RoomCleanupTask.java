package com.kabutar.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RoomCleanupTask {

    private final RoomCleanupService cleanup;

    public RoomCleanupTask(RoomCleanupService cleanup) {
        this.cleanup = cleanup;
    }

    @Scheduled(fixedRate = 30000) // every 30 sec
    public void cleanup() {
        cleanup.cleanupRooms();
    }
}