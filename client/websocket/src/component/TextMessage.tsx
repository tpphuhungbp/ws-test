import React, { useState } from "react";

const TextMessage = () => {
  const [socket, setSocket] = useState<WebSocket | null>(null);
  const [message, setMessage] = useState("");
  const [responses, setResponses] = useState<string[]>([]);
  const [isConnected, setIsConnected] = useState<boolean>(false);

  const connectWebSocket = () => {
    // const ws = new WebSocket("ws://tpphuhungbp.xyz/chat-socket-endpoint"); // Replace with your WebSocket URL
    const ws = new WebSocket("ws://localhost:8080/chat-socket-endpoint"); // Replace with your WebSocket URL

    ws.onopen = () => {
      console.log("WebSocket connection opened");
      setIsConnected(true);
    };

    ws.onmessage = (event) => {
      setResponses((prevResponses) => [...prevResponses, event.data]);
    };

    ws.onclose = () => {
      console.log("WebSocket connection closed");
      setIsConnected(false);
    };

    setSocket(ws);
  };

  const disconnectWebSocket = () => {
    if (socket) {
      socket.close();
      setSocket(null);
    }
  };

  const toggleConnection = () => {
    if (isConnected) {
      disconnectWebSocket();
    } else {
      connectWebSocket();
    }
  };

  const sendMessage = () => {
    if (socket && message) {
      socket.send(message);
      setMessage("");
    }
  };

  return (
    <div>
      <h2>WebSocket Chat</h2>
      <div>
        <button onClick={toggleConnection}>{isConnected ? "Disconnect" : "Connect"}</button>
      </div>
      {isConnected && (
        <div>
          <input
            type="text"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder="Type a message"
          />
          <button onClick={sendMessage}>Send</button>
        </div>
      )}
      <div>
        <h3>Responses:</h3>
        <ul>
          {responses.map((response, index) => (
            <li key={index}>{response}</li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default TextMessage;
