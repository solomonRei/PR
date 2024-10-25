package com.pr.parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        log.info("User {} joined the chat.", session.getId());
        broadcastMessage("User " + session.getId() + " joined the chat.");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String userMessage = "User " + session.getId() + ": " + message.getPayload();
        log.info(userMessage);
        broadcastMessage(userMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session.getId());
        broadcastMessage("User " + session.getId() + " left the chat.");
    }

    private void broadcastMessage(String message) {
        for (WebSocketSession session : sessions.values()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


