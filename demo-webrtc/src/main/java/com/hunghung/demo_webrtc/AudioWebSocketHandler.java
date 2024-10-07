package com.hunghung.demo_webrtc;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


@Component
public class AudioWebSocketHandler extends BinaryWebSocketHandler{
    private FileOutputStream fileOutputStream;
    private final AtomicBoolean recording = new AtomicBoolean(false);

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session); // Add new session to the set
        fileOutputStream = new FileOutputStream("received-audio.webm");
        recording.set(true);
        System.out.println("Connection established. Recording audio...");
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        ByteBuffer byteBuffer = message.getPayload();// Get the byte array from the message
        byte[] audioData = new byte[byteBuffer.remaining()];
        byteBuffer.get(audioData);

        // Broadcast the audio data to all connected clients

        broadcastAudioData(audioData);
        // Additionally write the received binary audio data to the file if recording is true
        if(recording.get()) {

            fileOutputStream.write(audioData);
            fileOutputStream.flush();
        }



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

        //Additionally
        if (fileOutputStream != null) {
            recording.set(false);
            fileOutputStream.close();
            System.out.println("Connection closed. Audio saved.");
        }
    }
}

