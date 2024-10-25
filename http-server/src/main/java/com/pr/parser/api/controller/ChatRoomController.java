package com.pr.parser.api.controller;

import com.pr.parser.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final WebSocketService webSocketService;

    @PostMapping("/join/{username}")
    public ResponseEntity<String> joinChatRoom(@PathVariable String username) {
        String joinMessage = username + " joined the chat room.";
        webSocketService.sendMessage(joinMessage);
        return ResponseEntity.ok(joinMessage);
    }

    @PostMapping("/leave/{username}")
    public ResponseEntity<String> leaveChatRoom(@PathVariable String username) {
        String leaveMessage = username + " left the chat room.";
        webSocketService.sendMessage(leaveMessage);
        return ResponseEntity.ok(leaveMessage);
    }

    @PostMapping("/send/{username}")
    public ResponseEntity<String> sendMessage(
            @PathVariable String username,
            @RequestParam String message) {

        String userMessage = username + ": " + message;
        webSocketService.sendMessage(userMessage);
        return ResponseEntity.ok("Message sent: " + userMessage);
    }
}

