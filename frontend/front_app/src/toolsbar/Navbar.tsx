import "./Navbar.css";
import { Toggle } from "./Toggle";
import LogIn from "./LogIn/LogIn";
import { useState } from "react";
import { Link } from "react-router-dom";

const Navbar = ({ theme, setTheme, isLoggedIn, setIsLoggedIn }: { theme: string; setTheme: Function; isLoggedIn: boolean;
  setIsLoggedIn: Function;}) => {
  const [showLogIn, setShowLogIn] = useState(false); // Starea pentru deschiderea/închiderea modalului

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
      <div className="logo">
        <Link to="/" className="logo">
            <img src="./1-removebg-preview.png" alt="logo" />
        </Link>
      </div>

      <div className="pos_button_1">
        {isLoggedIn ? (
          <>
          <Link to="/profile">
            <button className="button">
              <h1>Profile</h1>
            </button>
          </Link>
          <Link to="/">
          <button className="button">
            <h1>Home</h1>
          </button>
          </Link>
          </>
        ) : (
        <button className="button" onClick={() => setShowLogIn(true)}>
          <h1>Log in</h1>
        </button>
        )}

        <Link to="/about">
          <button className="button">
            <h1>About us</h1> 
          </button>
        </Link>
        <Toggle
          label={label_theme}
          toggled={theme === "dark"}
          onClick={toggle_mode}
        />

        {isLoggedIn && (
          <button className="button" onClick={handleLogout}>
            <h1>Log out</h1>
          </button>
        )}
      </div>
      {/* Modalul de Login */}
      {showLogIn && <LogIn showlog={showLogIn} setShowlog={setShowLogIn} />}
    </div>
  );
};

export default Navbar;
