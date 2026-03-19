package com.kabutar.dto;

public class MatchResponse {

    private String roomId;

    public MatchResponse(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}