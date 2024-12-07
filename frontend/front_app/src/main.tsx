import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import "./index.css";
import ReactModal from 'react-modal';

// Setează elementul principal al aplicației (de obicei #root)
ReactModal.setAppElement('#root');


ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
