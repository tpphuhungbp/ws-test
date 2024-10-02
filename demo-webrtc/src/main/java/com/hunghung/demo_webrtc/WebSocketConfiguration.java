package com.hunghung.demo_webrtc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer{

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler( new TextMessageSocketHandler(), "/chat-socket-endpoint").setAllowedOrigins("*");
        registry.addHandler(new AudioWebSocketHandler(), "/audio-socket-endpoint")
                .setAllowedOrigins("*"); // WebSocket endpoint for audio
    }
}
