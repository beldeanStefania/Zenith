import React, { useState } from "react";
import Modal from "react-modal";
import axios from "axios";
import "./SingUp.css";

const SignUp = ({ show, setShow }: { show: boolean; setShow: Function }) => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [confirmEmail, setConfirmEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [spotifyAuthUrl, setSpotifyAuthUrl] = useState<string | null>(null); // Stocăm URL-ul de autorizare Spotify

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMessage(null);
    setSuccessMessage(null);

    if (email !== confirmEmail) {
      setErrorMessage("Emails do not match.");
      return;
    }
    if (password !== confirmPassword) {
      setErrorMessage("Passwords do not match.");
      return;
    }

    try {
      const response = await axios.post("http://localhost:8080/api/user/add", {
        username,
        email,
        password,
      });

      if (response.status === 200) {
        setSuccessMessage("Account created successfully. Please authorize Spotify.");

        // Fetch Spotify authorization URL without redirecting
        const loginResponse = await axios.get(
          `http://localhost:8080/api/spotify/login?username=${username}`
        );
        setSpotifyAuthUrl(loginResponse.data); // Salvăm URL-ul pentru a fi utilizat într-un link

        setUsername("");
        setEmail("");
        setConfirmEmail("");
        setPassword("");
        setConfirmPassword("");
      } else {
        setErrorMessage("Something went wrong. Please try again.");
      }
    } catch (error: any) {
      console.error("Error while signing up:", error);
      setErrorMessage(
        error.response?.status === 409
          ? "User with this username already exists."
          : "An unexpected error occurred. Please try again later."
      );
    }
  };

  return (
    <Modal
      isOpen={show}
      onRequestClose={() => setShow(false)}
      contentLabel="SignUp"
      overlayClassName="customOverlay"
      className="customModalSingUp"
    >
      <button
        type="button"
        className="close"
        aria-label="Close"
        onClick={() => setShow(false)}
      >
        <span aria-hidden="true">&times;</span>
      </button>

      <div className="signup">
        <div className="form">
          <h1>Sign Up</h1>
          <form onSubmit={handleSubmit}>
            <label htmlFor="username" className="position">
              Username
            </label>
            <div className="form-group">
              <input
                type="text"
                className="form-user"
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="Enter Username"
                required
              />
            </div>

            <label htmlFor="email" className="position">
              Email address
            </label>
            <div className="form-group">
              <input
                type="email"
                className="form-control"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Enter email"
                required
              />
              <input
                type="email"
                className="form-control"
                id="confirmEmail"
                value={confirmEmail}
                onChange={(e) => setConfirmEmail(e.target.value)}
                placeholder="Confirm email"
                required
              />
            </div>

            <label htmlFor="password" className="position">
              Password
            </label>
            <div className="form-group">
              <input
                type="password"
                className="form-control"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter password"
                required
              />
              <input
                type="password"
                className="form-control"
                id="confirmPassword"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                placeholder="Confirm password"
                required
              />
            </div>

            {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
            {successMessage && <p style={{ color: "green" }}>{successMessage}</p>}
            {spotifyAuthUrl && <a href={spotifyAuthUrl} target="_blank">Authorize Spotify</a>}  

            <button type="submit" className="btn-singup">Sign Up</button>
          </form>
        </div>
      </div>
    </Modal>
  );
};

export default SignUp;
