package com.pr.parser.api.controller;

import com.pr.parser.service.WebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Chat Room", description = "API для управления чат-комнатой")
public class ChatRoomController {

    private final WebSocketService webSocketService;

    @Operation(summary = "Присоединиться к чат-комнате")
    @PostMapping("/join/{username}")
    public ResponseEntity<String> joinChatRoom(
            @Parameter(description = "Имя пользователя") @PathVariable String username) {
        String joinMessage = username + " joined the chat room.";
        webSocketService.sendMessage(joinMessage);
        return ResponseEntity.ok(joinMessage);
    }

    @Operation(summary = "Покинуть чат-комнату")
    @PostMapping("/leave/{username}")
    public ResponseEntity<String> leaveChatRoom(
            @Parameter(description = "Имя пользователя") @PathVariable String username) {
        String leaveMessage = username + " left the chat room.";
        webSocketService.sendMessage(leaveMessage);
        return ResponseEntity.ok(leaveMessage);
    }

    @Operation(summary = "Отправить сообщение в чат-комнату")
    @PostMapping("/send/{username}")
    public ResponseEntity<String> sendMessage(
            @Parameter(description = "Имя пользователя") @PathVariable String username,
            @Parameter(description = "Сообщение для отправки") @RequestParam String message) {

        String userMessage = username + ": " + message;
        webSocketService.sendMessage(userMessage);
        return ResponseEntity.ok("Message sent: " + userMessage);
    }
}

