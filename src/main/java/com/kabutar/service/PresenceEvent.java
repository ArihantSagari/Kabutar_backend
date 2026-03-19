package com.kabutar.service;

public class PresenceEvent {

    private String user;
    private boolean online;
    private long lastSeen;

    public PresenceEvent(String user, boolean online, long lastSeen) {
        this.user = user;
        this.online = online;
        this.lastSeen = lastSeen;
    }

    public String getUser() { return user; }
    public boolean isOnline() { return online; }
    public long getLastSeen() { return lastSeen; }
}