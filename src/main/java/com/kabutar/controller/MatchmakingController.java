package com.kabutar.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.kabutar.service.MatchmakingService;

@Controller
public class MatchmakingController {

    private final MatchmakingService matchmakingService;

    public MatchmakingController(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }
    @MessageMapping("/chat/matchmaking")
    public void matchmaking(Principal principal) {

        if (principal == null) {
            throw new IllegalStateException("Unauthorized");
        }

        String username = principal.getName();

        System.out.println("🎯 Matchmaking request from: " + username);

        matchmakingService.enqueueUser(username);
    }

}