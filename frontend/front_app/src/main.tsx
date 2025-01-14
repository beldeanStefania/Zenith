/**
 * @fileoverview Entry point for the React application, sets up the root render and modal configuration
 * @requires react
 * @requires react-dom/client
 * @requires ReactModal
 */

import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import "./index.css";
import ReactModal from 'react-modal';

/**
 * Sets the app element for ReactModal accessibility features
 * This is required for screen readers and accessibility compliance
 */
ReactModal.setAppElement('#root');

/**
 * Creates and renders the root React component within StrictMode
 * StrictMode enables additional development-only checks for potential problems
 */
ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);