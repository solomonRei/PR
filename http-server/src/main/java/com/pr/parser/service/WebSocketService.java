package com.pr.parser.service;

import com.pr.parser.rest.CustomWebSocketClient;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class WebSocketService {

    private CustomWebSocketClient webSocketClient;

    @PostConstruct
    public void init() throws Exception {
        webSocketClient = new CustomWebSocketClient(new URI("ws://localhost:8081/ws/chat"));
        webSocketClient.connect();
    }

    public void sendMessage(String message) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.sendMessageToServer(message);
        } else {
            System.out.println("WebSocket is not connected.");
        }
    }

    @PreDestroy
    public void close() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}
