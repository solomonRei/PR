package com.pr.parser.rest;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class CustomWebSocketClient extends WebSocketClient {

    public CustomWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to WebSocket server.");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message from WebSocket server: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from WebSocket server.");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void sendMessageToServer(String message) {
        send(message);
    }
}
