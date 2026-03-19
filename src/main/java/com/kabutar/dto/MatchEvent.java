package com.kabutar.dto;

public class MatchEvent {

    private String roomId;
    private String partnerId;

    public MatchEvent(String roomId, String partnerId){
        this.roomId = roomId;
        this.partnerId = partnerId;
    }

    public String getRoomId(){
        return roomId;
    }

    public String getPartnerId(){
        return partnerId;
    }
}