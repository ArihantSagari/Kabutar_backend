package com.kabutar.model;

public enum ChatEventType {

    MESSAGE,
    TYPING,
    STOP_TYPING,
    DELIVERED,
    SEEN,

    SKIP,
    END,

    USER_LEFT,

    PRESENCE,
    HEARTBEAT
}