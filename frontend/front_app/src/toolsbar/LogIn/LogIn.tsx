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
      // 1. Apelează endpoint-ul de login din backend
      const response = await axios.post("http://localhost:8080/api/auth/login", {
        username,
        password,
      });

      // 2. Răspunsul e un string = token JWT
      const token = response.data;

      // 3. Verificăm să fie un string valid
      if (typeof token === "string" && token.startsWith("ey")) {
        // 4. Salvează token și username în localStorage
        localStorage.setItem("token", token);
        localStorage.setItem("username", username);

        // 5. Închide dialogul
        setShowlog(false);
      } else {
        setLoginError("Invalid response from server.");
      }
    } catch (error: any) {
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
      {/* Buton de închidere a modalei */}
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
          {/* Formular de login */}
          <form onSubmit={handleSubmit}>
            {/* Username field */}
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

            {/* Password field */}
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

            {/* Afișare eroare dacă e cazul */}
            {loginError && <p style={{ color: "red" }}>{loginError}</p>}

            {/* Submit button */}
            <button type="submit" className="btn-login">
              Submit
            </button>
          </form>
          <a href="#" className="forgot">
            Forgot Password?
          </a>
        </div>
      </div>
    </Modal>
  );
};

export default LogIn;
