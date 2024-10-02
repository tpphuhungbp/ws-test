import React from "react";
import { Link } from "react-router-dom";

const Home = () => {
  return (
    <div>
      <h1>Home</h1>

      <Link to="/text-message">
        <button>Go to Text Message</button>
      </Link>
      <Link to="/audio-streaming">
        <button>Go to Audio Streaming</button>
      </Link>
    </div>
  );
};
export default Home;
