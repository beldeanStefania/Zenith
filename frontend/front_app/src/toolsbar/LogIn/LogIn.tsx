import React, { useState } from "react";
import Modal from "react-modal";
import axios from "axios";
import "./LogIn.css";

interface LogInProps {
  showlog: boolean;
  setShowlog: (show: boolean) => void;
}

const LogIn: React.FC<LogInProps> = ({ showlog, setShowlog }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loginError, setLoginError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoginError(null);

    try {
      // Login request
      const response = await axios.post("http://localhost:8080/api/auth/login", {
        username,
        password,
      });

      const token = response.data; // Assuming token is returned correctly

      if (typeof token === "string" && token.startsWith("ey")) { // Better to check for a proper JWT structure
        localStorage.setItem("token", token);
        localStorage.setItem("username", username);

        // Trigger Spotify login only after successful local login
        const spotifyAuthResponse = await axios.get(`http://localhost:8080/api/spotify/login?username=${username}`);
        window.location.href = spotifyAuthResponse.data; // Redirect to Spotify for authorization

        setShowlog(false);
      } else {
        setLoginError("Invalid response from server.");
      }
    } catch (error) {
      console.error("Login error:", error);
      setLoginError("Invalid username or password.");
    }
  };

  return (
    <Modal
      isOpen={showlog}
      onRequestClose={() => setShowlog(false)}
      contentLabel="LogIn"
      overlayClassName="customOverlay"
      className="customModalLogIn"
    >
      <button
        type="button"
        className="close"
        aria-label="Close"
        onClick={() => setShowlog(false)}
      >
        <span aria-hidden="true">&times;</span>
      </button>

      <div className="login">
        <div className="form">
          <h1>Log In</h1>
          <form onSubmit={handleSubmit}>
            <label htmlFor="user" className="position">
              Username
            </label>
            <input
              type="text"
              className="form-user-login"
              id="user"
              placeholder="Enter Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />

            <label htmlFor="password" className="position">
              Password
            </label>
            <input
              type="password"
              className="form-control-login"
              id="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />

            {loginError && <p style={{ color: "red" }}>{loginError}</p>}

            <button type="submit" className="btn-login">
              Log In
            </button>
          </form>
        </div>
      </div>
    </Modal>
  );
};

export default LogIn;