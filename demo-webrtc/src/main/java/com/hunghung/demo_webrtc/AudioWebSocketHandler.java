package com.hunghung.demo_webrtc;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
public class AudioWebSocketHandler extends BinaryWebSocketHandler{
    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session); // Add new session to the set
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        byte[] audioData = message.getPayload().array(); // Get the byte array from the message
        broadcastAudioData(audioData); // Broadcast the audio data to all connected clients
    }

    private void broadcastAudioData(byte[] audioData) {
        // Broadcast audio data to all sessions
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new BinaryMessage(audioData)); // Send the audio data as a binary message
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session); // Remove session from the set
    }
}

