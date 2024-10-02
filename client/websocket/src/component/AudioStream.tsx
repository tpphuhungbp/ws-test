import React, { useRef, useState } from "react";

export default function AudioStream() {
  const [audioSocket, setAudioSocket] = useState<WebSocket | null>(null);
  const mediaRecorderRef = useRef<MediaRecorder | null>(null);
  const [isRecording, setIsRecording] = useState(false);
  const [isConnected, setIsConnected] = useState(false);
  const [receivedData, setReceivedData] = useState<Uint8Array | null>(null);
  const [audioUrl, setAudioUrl] = useState<string | null>(null); // State to hold audio URL

  const connectWebSocket = () => {
    const socket = new WebSocket("ws://tpphuhungbp.xyz/audio-socket-endpoint");
    // const socket = new WebSocket("ws://localhost:8080/audio-socket-endpoint");

    socket.onopen = () => {
      console.log("Connected to WebSocket server");
      setAudioSocket(socket);
      setIsConnected(true);
    };

    socket.onmessage = (event) => {
      const reader = new FileReader();
      reader.onload = () => {
        const audioData = new Uint8Array(reader.result as ArrayBuffer);
        setReceivedData(audioData);

        // Create a Blob and an object URL for audio playback
        const blob = new Blob([audioData], { type: "audio/webm; codecs=opus" });
        const url = URL.createObjectURL(blob);
        setAudioUrl(url); // Set the audio URL for playback

        console.log("Received audio data:", audioData);
      };
      reader.readAsArrayBuffer(event.data); // Read received Blob as ArrayBuffer
    };

    socket.onclose = () => {
      console.log("Disconnected from WebSocket server");
      setAudioSocket(null);
      setIsConnected(false);
    };
  };

  const disconnectWebSocket = () => {
    if (audioSocket) {
      audioSocket.close();
    }
  };

  const startRecording = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      mediaRecorderRef.current = new MediaRecorder(stream);

      mediaRecorderRef.current.ondataavailable = (event) => {
        if (audioSocket && audioSocket.readyState === WebSocket.OPEN) {
          console.log("Sending audio chunk:", event.data);
          audioSocket.send(event.data); // Send audio chunk over WebSocket
        }
      };

      mediaRecorderRef.current.start(100); // Start recording
      setIsRecording(true);
    } catch (error) {
      console.error("Error accessing microphone:", error);
    }
  };

  const stopRecording = () => {
    if (mediaRecorderRef.current) {
      mediaRecorderRef.current.stop();
      setIsRecording(false);
    }
  };

  return (
    <div>
      <h1>Audio Streamer</h1>
      <button onClick={isConnected ? disconnectWebSocket : connectWebSocket}>
        {isConnected ? "Disconnect" : "Connect"}
      </button>
      <br />
      <button onClick={startRecording} disabled={isRecording || !isConnected}>
        Start Streaming
      </button>
      <button onClick={stopRecording} disabled={!isRecording}>
        Stop Streaming
      </button>
      <h2>Received Audio Data:</h2>
      {receivedData ? (
        <pre>{JSON.stringify(Array.from(receivedData), null, 2)}</pre>
      ) : (
        <p>No audio data received.</p>
      )}
    </div>
  );
}
