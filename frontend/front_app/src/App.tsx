import { useEffect, useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import "./App.css";
import Navbar from "./toolsbar/Navbar";
import SlideShower from "./MainPage/SlideShower";
import Background from "./MainPage/Stars/Background";
import StarLayer from "./MainPage/Stars/StarLayer";
import Profile from "./components/Profile/Profile";
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import { generateBoxShadows } from "./MainPage/Stars/utils";

function App() {
  const currentTheme = localStorage.getItem("current_theme") || "light";
  const [theme, setTheme] = useState(currentTheme);
  const [showSurvey, setShowSurvey] = useState(false);
  const [showSingUp, setShowSingUp] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    // Check if user is logged in by verifying JWT token
    const token = localStorage.getItem('token');
    setIsLoggedIn(!!token);

    localStorage.setItem("current_theme", theme);
    document.documentElement.setAttribute("theme", theme);
  }, [theme]);

  return (
    <Router>
      <Background>
        {/* Star layers for background effect */}
        <StarLayer size={1} duration={50} shadows={generateBoxShadows(700)} />
        <StarLayer size={2} duration={100} shadows={generateBoxShadows(200)} />
        <StarLayer size={3} duration={150} shadows={generateBoxShadows(100)} />

        {/* Navigation bar */}
        <Navbar
          theme={theme}
          setTheme={setTheme}
          isLoggedIn={isLoggedIn}
          setIsLoggedIn={setIsLoggedIn}
        />
        
        {/* Route configuration */}
        <Routes>
          {/* Home route */}
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

          {/* Protected profile route */}
          <Route
            path="/profile"
            element={
              <PrivateRoute>
                <Profile />
              </PrivateRoute>
            }
          />
          
          {/* Add a catch-all redirect to home page */}
          <Route 
            path="*" 
            element={<Navigate to="/" replace />} 
          />
        </Routes>
      </Background>
    </Router>
  );
}

export default App;