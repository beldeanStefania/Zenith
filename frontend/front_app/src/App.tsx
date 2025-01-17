import { useEffect, useState } from "react";
import { BrowserRouter as Router, Route, Navigate } from "react-router-dom";
import { Routes } from "react-router";
import "./App.css";
import Navbar from "./toolsbar/Navbar";
import SlideShower from "./MainPage/SlideShower";
import Background from "./MainPage/Stars/Background";
import StarLayer from "./MainPage/Stars/StarLayer";
import { generateBoxShadows } from "./MainPage/Stars/utils";
import Profile from "./Profile/Profile";
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import ZenithIntro from "./components/ZenithIntro/ZenithIntro";
import About from "./About/About";

/**
 * Main application component that manages the application state and routing
 * @component
 * @returns {JSX.Element} The rendered App component
 */
function App() {
  // Initialize theme from localStorage or default to 'light'
  const currentTheme = localStorage.getItem("current_theme") || "light";
  /**
   * State management for various application features
   */
  const [theme, setTheme] = useState(currentTheme);
  const [showSurvey, setShowSurvey] = useState(false);
  const [showSingUp, setShowSingUp] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [showIntro, setShowIntro] = useState(true);
  const [fadeOut, setFadeOut] = useState(false);

  /**
   * Effect hook to handle authentication state and theme persistence
   */
  useEffect(() => {
    const token = localStorage.getItem('token');
    setIsLoggedIn(!!token);

    localStorage.setItem("current_theme", theme);
    document.documentElement.setAttribute("theme", theme);
  }, [theme]);

  /**
   * Handles the completion of the intro animation
   * @function
   */
  const handleIntroComplete = () => {
    setFadeOut(true);
    setTimeout(() => {
      setShowIntro(false);
    }, 1000);
  };

  if (showIntro) {
    return (
      <div style={{
        opacity: fadeOut ? 0 : 1,
        transition: 'opacity 1s ease-out'
      }}>
        <ZenithIntro onComplete={handleIntroComplete} />
      </div>
    );
  }

  return (
    <Router>
      <Background>
        <StarLayer size={1} duration={50} shadows={generateBoxShadows(700)} />
        <StarLayer size={2} duration={100} shadows={generateBoxShadows(200)} />
        <StarLayer size={3} duration={150} shadows={generateBoxShadows(100)} />

        <div>
          <Navbar
            theme={theme}
            setTheme={setTheme}
            isLoggedIn={isLoggedIn}
            setIsLoggedIn={setIsLoggedIn}
          />
        </div>
        <Routes>
          <Route 
            path="/" 
            element={
                <div className="slider">
                  <SlideShower
                    show={showSurvey}
                    setShowSurvey={setShowSurvey}
                    showSingUp={showSingUp}
                    setShowSingUp={setShowSingUp}
                  />
                  </div>
            } 
          />
        <Route
            path="/profile"
            element={
              <PrivateRoute>
                <Profile />
              </PrivateRoute>
            }
          />
          <Route 
            path="*" 
            element={<Navigate to="/" replace />} 
          />
          <Route
            path = "/about"
            element = {
              <About/>
            }
          />
        </Routes>
      </Background>
    </Router>
  );
}

export default App;
