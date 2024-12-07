import { useEffect, useState } from "react";
import "./App.css";
import Navbar from "./toolsbar/Navbar";
import SlideShower from "./MainPage/SlideShower";
import Background from "./MainPage/Stars/Background";
import StarLayer from "./MainPage/Stars/StarLayer";
import { generateBoxShadows } from "./MainPage/Stars/utils";

function App() {
  const currentTheme = localStorage.getItem("current_theme") || "light";
  const [theme, setTheme] = useState(currentTheme);
  const [showSurvey, setShowSurvey] = useState(false);
  const [showSingUp, setShowSingUp] = useState(false);

  useEffect(() => {
    localStorage.setItem("current_theme", theme);
    document.documentElement.setAttribute("theme", theme);
  }, [theme]);

  return (
    <>
      <Background>
        <StarLayer size={1} duration={50} shadows={generateBoxShadows(700)} />
        <StarLayer size={2} duration={100} shadows={generateBoxShadows(200)} />
        <StarLayer size={3} duration={150} shadows={generateBoxShadows(100)} />

        <div>
          <Navbar
            theme={theme}
            setTheme={setTheme}
          />
        </div>
        <div className="slider">
          <SlideShower
            show={showSurvey}
            setShowSurvey={setShowSurvey}
            showSingUp={showSingUp}
            setShowSingUp={setShowSingUp}
          />
        </div>
      </Background>
    </>
  );
}

export default App;
