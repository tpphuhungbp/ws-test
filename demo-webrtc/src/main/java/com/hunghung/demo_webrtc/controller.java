package com.hunghung.demo_webrtc;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class controller {
    @RequestMapping("/")
    String home() {
        return "server is running!";
    }
}
