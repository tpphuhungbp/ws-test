package com.hunghung.demo_webrtc;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @RequestMapping("/")
    String home() {
        return "server is running!";
    }
    private static final String AUDIO_FILE_PATH = "audio_received.raw"; // Change this to your audio file path

    @GetMapping("/audio")
    public ResponseEntity<Resource>  getAudio() throws Exception {
        Path path = Paths.get("received-audio.webm");
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists()) {
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("audio/webm")) // Ensure correct MIME type
                        .body(resource);
            } else {
                throw new RuntimeException("File not found");
            }

        } else {
            System.out.println("file not found...");

            throw new RuntimeException("File not found");
        }
    }

}
