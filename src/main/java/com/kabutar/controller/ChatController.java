package com.kabutar.controller;

import com.kabutar.service.MatchmakingService;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final MatchmakingService matchmakingService;

    public ChatController(MatchmakingService matchmakingService){
        this.matchmakingService = matchmakingService;
    }


    /* ------------------------------------------------ */
    /* START MATCHMAKING                                */
    /* ------------------------------------------------ */

    @PostMapping("/start")
    public Map<String,Object> start(Principal principal){

        Map<String,Object> res = new HashMap<>();

        if(principal == null){

            res.put("error","User not authenticated");

            return res;
        }

        String userId = principal.getName();

        System.out.println("User joined matchmaking: " + userId);

        String roomId = matchmakingService.enqueueUser(userId);

        if(roomId != null){

            res.put("matched",true);
            res.put("roomId",roomId);

        } else {

            res.put("matched",false);

        }

        return res;

    }

}