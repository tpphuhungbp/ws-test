import React from "react";
import logo from "./logo.svg";
import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./component/Home";
import TextMessage from "./component/TextMessage";
import AudioStream from "./component/AudioStream";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/text-message" element={<TextMessage />} />
        <Route path="/audio-streaming" element={<AudioStream />} />
        <Route path="*" element={<Home />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
