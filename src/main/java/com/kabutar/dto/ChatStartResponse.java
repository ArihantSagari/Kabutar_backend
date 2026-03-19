package com.kabutar.dto;

public class ChatStartResponse {

    private boolean matched;
    private String partner;

    public ChatStartResponse(boolean matched, String partner) {
        this.matched = matched;
        this.partner = partner;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }
}