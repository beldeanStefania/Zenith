/**
 * @fileoverview Navigation bar component that handles app navigation and theme switching
 * @requires react
 * @requires react-router-dom
 */

import "./Navbar.css";
import { Toggle } from "./Toggle";
import LogIn from "./LogIn/LogIn";
import { useState } from "react";
import { Link } from "react-router-dom";

/**
 * Interface for Navbar component props
 * @interface NavbarProps
 * @property {string} theme - Current theme ('light' or 'dark')
 * @property {Function} setTheme - Function to update the theme
 * @property {boolean} isLoggedIn - User authentication status
 * @property {Function} setIsLoggedIn - Function to update authentication status
 */
interface NavbarProps {
  theme: string;
  setTheme: Function;
  isLoggedIn: boolean;
  setIsLoggedIn: Function;
}

/**
 * Navigation bar component that provides navigation links, theme toggling,
 * and authentication controls
 * 
 * @component
 * @param {NavbarProps} props - Component props
 * @returns {JSX.Element} The navigation bar UI
 */
const Navbar = ({ theme, setTheme, isLoggedIn, setIsLoggedIn }: NavbarProps) => {
  /**
   * State to control login modal visibility
   */
  const [showLogIn, setShowLogIn] = useState(false);

  /**
   * Toggles between light and dark theme
   * @function
   */
  const toggle_mode = () => {
    theme === "light" ? setTheme("dark") : setTheme("light");
  };

  /**
   * Gets the label for the theme toggle button
   */
  const label_theme = theme === "light" ? "Darkmode" : "Lightmode";

  /**
   * Handles user logout
   * Removes authentication tokens and updates login state
   * @function
   */
  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    setIsLoggedIn(false);
  };

  return (
    <div className="navbar">
      {/* Logo and home link */}
      <div className="logo">
        <Link to="/" className="logo">
            <img src="./1-removebg-preview.png" alt="logo" />
        </Link>
      </div>

      {/* Navigation buttons and controls */}
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
        
        {/* Theme toggle control */}
        <Toggle
          label={label_theme}
          toggled={theme === "dark"}
          onClick={toggle_mode}
        />

        {/* Logout button for authenticated users */}
        {isLoggedIn && (
          <button className="button" onClick={handleLogout}>
            <h1>Log out</h1>
          </button>
        )}
      </div>

      {/* Login modal */}
      {showLogIn && <LogIn showlog={showLogIn} setShowlog={setShowLogIn} />}
    </div>
  );
};

export default Navbar;