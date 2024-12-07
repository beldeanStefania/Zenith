import "./Navbar.css";
import { Toggle } from "./Toggle";
import LogIn from "./LogIn/LogIn";
import { useState } from "react";
import Waveform from "./Waveform";

const Navbar = ({ theme, setTheme }: { theme: string; setTheme: Function }) => {
  const [showLogIn, setShowLogIn] = useState(false); // Starea pentru deschiderea/închiderea modalului

  const toggle_mode = () => {
    theme === "light" ? setTheme("dark") : setTheme("light");
  };

  const label_theme = theme === "light" ? "Darkmode" : "Lightmode";

  return (
    <div className="navbar">
      <div className="logo">
         <img src="./1-removebg-preview.png" alt="logo" /> 
      </div>

      <div className="pos_button_1">
        <button className="button" onClick={() => setShowLogIn(true)}>
          <h1>Log in</h1>
        </button>

        <button className="button">
          <h1>About us</h1>
        </button>
        <Toggle
          label={label_theme}
          toggled={theme === "dark"}
          onClick={toggle_mode}
        />
      </div>
      {/* Modalul de Login */}
      {showLogIn && <LogIn showlog={showLogIn} setShowlog={setShowLogIn} />}
    </div>
  );
};

export default Navbar;
