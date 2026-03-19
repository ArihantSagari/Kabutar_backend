package com.kabutar.dto;

public class MessageStatusDTO {

    private String messageId;
    private long timestamp; // REQUIRED for seen logic

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}