/**
 * @fileoverview LogIn component that handles user authentication
 * @requires react
 * @requires react-modal
 * @requires axios
 */

import React, { useState } from "react";
import Modal from "react-modal";
import axios from "axios";
import "./LogIn.css";

/**
 * Interface for LogIn component props
 * @interface LogInProps
 * @property {boolean} showlog - Controls the visibility of the login modal
 * @property {(show: boolean) => void} setShowlog - Function to update the modal's visibility
 */
interface LogInProps {
  showlog: boolean;
  setShowlog: (show: boolean) => void;
}

/**
 * Login component that provides user authentication functionality
 * Handles user login and Spotify authorization
 * 
 * @component
 * @param {LogInProps} props - Component props
 * @returns {JSX.Element} A modal containing the login form
 */
const LogIn: React.FC<LogInProps> = ({ showlog, setShowlog }) => {
  /**
   * State management for form fields and status
   */
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loginError, setLoginError] = useState<string | null>(null);
  const [spotifyAuthUrl, setSpotifyAuthUrl] = useState<string | null>(null);

  /**
   * Handles form submission for user login
   * Makes API calls for authentication and Spotify authorization
   * 
   * @async
   * @param {React.FormEvent} e - Form submission event
   */
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoginError(null);

    try {
      // Attempt login
      const response = await axios.post(
        "http://localhost:8080/api/auth/login",
        {
          username,
          password,
        }
      );

      const token = response.data;

      // Validate and process token
      if (typeof token === "string" && token.startsWith("ey")) {
        localStorage.setItem("token", token);
        localStorage.setItem("username", username);

        // Get Spotify authorization URL
        const spotifyAuthResponse = await axios.get(
          `http://localhost:8080/api/spotify/login?username=${username}`
        );
        setSpotifyAuthUrl(spotifyAuthResponse.data);

        setShowlog(false);
        window.location.reload();
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
          <div className="line">
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
              {spotifyAuthUrl && (
                <div>
                  <a href={spotifyAuthUrl} target="_blank">
                    Continue to Spotify
                  </a>
                </div>
              )}

              <button type="submit" className="btn-login">
                Log In
              </button>
            </form>
          </div>
        </div>
      </div>
    </Modal>
  );
};

export default LogIn;