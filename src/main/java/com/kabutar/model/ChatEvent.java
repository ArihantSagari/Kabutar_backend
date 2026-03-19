package com.kabutar.model;

/*
=====================================================
CHAT EVENT MODEL
-----------------------------------------------------
Represents all WebSocket events in Kabutar chat.

Supported event types:
MESSAGE
TYPING
DELIVERED
SEEN
SKIP
END
=====================================================
*/

public class ChatEvent {

    /* Unique event ID */
    private String id;

    /* Event type (MESSAGE, TYPING, etc) */
    private ChatEventType type;

    /* Chat room identifier */
    private String roomId;

    /* Username of sender */
    private String sender;

    /* Text message content */
    private String content;

    /* Message ID (used for delivered/seen status) */
    private String messageId;

    /* Event timestamp */
    private long timestamp;

    private boolean online;
    /* -----------------------------
       Default Constructor
    ----------------------------- */

    public ChatEvent() {}


    /* -----------------------------
       Getters & Setters
    ----------------------------- */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChatEventType getType() {
        return type;
    }

    public void setType(ChatEventType type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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


	public boolean isOnline() {
		return online;
	}


	public void setOnline(boolean online) {
		this.online = online;
	}

}