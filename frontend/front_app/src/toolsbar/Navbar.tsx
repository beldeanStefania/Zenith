import "./Navbar.css";
import { Toggle } from "./Toggle";
import LogIn from "./LogIn/LogIn";
import { useState } from "react";
import { Link } from "react-router-dom";

const Navbar = ({ theme, setTheme, isLoggedIn, setIsLoggedIn }: { 
  theme: string; 
  setTheme: Function;
  isLoggedIn: boolean;
  setIsLoggedIn: Function;
}) => {
  const [showLogIn, setShowLogIn] = useState(false);

  const toggle_mode = () => {
    theme === "light" ? setTheme("dark") : setTheme("light");
  };

  const label_theme = theme === "light" ? "Darkmode" : "Lightmode";

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    setIsLoggedIn(false);
  };

  return (
    <div className="navbar">
      <div className="logo-section">
        <div className="logo">
          <img src="./1-removebg-preview.png" alt="logo" />
        </div>
        <div className="profile-circle">
          <img src="./human-profile-with-music-note-vector-13908950-removebg-preview.png" alt="Profile" />
        </div>
      </div>

      <div className="pos_button_1">
        {isLoggedIn ? (
          <>
            <Link to="/profile" className="button">
              <h1>Profile</h1>
            </Link>
            <button className="button" onClick={handleLogout}>
              <h1>Log out</h1>
            </button>
          </>
        ) : (
          <button className="button" onClick={() => setShowLogIn(true)}>
            <h1>Log in</h1>
          </button>
        )}

        <button className="button">
          <h1>About us</h1>
        </button>
        <Toggle
          label={label_theme}
          toggled={theme === "dark"}
          onClick={toggle_mode}
        />
      </div>
      
      {showLogIn && <LogIn showlog={showLogIn} setShowlog={setShowLogIn} />}
    </div>
  );
};

export default Navbar;