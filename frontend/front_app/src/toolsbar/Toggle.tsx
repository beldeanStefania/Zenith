/**
 * @fileoverview Toggle component for switching between different states
 * @requires react
 */

import { useState } from "react";
import "./Toggle.css";

/**
 * Interface for Toggle component props
 * @interface ToggleProps
 * @property {string} label - Text to display on the toggle button
 * @property {boolean} toggled - Initial toggle state
 * @property {Function} onClick - Callback function when toggle is clicked
 */
interface ToggleProps {
  label: string;
  toggled: boolean;
  onClick: Function;
}

/**
 * Toggle component that provides a button-style toggle control
 * Can be used for theme switching or any binary state toggle
 * 
 * @component
 * @param {ToggleProps} props - Component props
 * @returns {JSX.Element} A styled button that acts as a toggle
 * 
 * @example
 * ```tsx
 * <Toggle
 *   label="Dark Mode"
 *   toggled={isDarkMode}
 *   onClick={handleThemeChange}
 * />
 * ```
 */
export const Toggle = ({
  label,
  toggled,
  onClick,
}: ToggleProps) => {
  /**
   * Local state to manage toggle status
   * @state
   */
  const [isToggled, toggle] = useState(toggled);

  /**
   * Handles the toggle action
   * Updates local state and calls the provided onClick callback
   * @function
   */
  const callback = () => {
    toggle(!isToggled);
    onClick(!isToggled);
  };

  return (
    // Note: Original checkbox implementation is commented out but preserved for reference
    // <label>
    //   <input type="checkbox" defaultChecked={isToggled} onClick={callback} />
    //   <span />
    // </label>
    <button className="button" onClick={() => callback()}>
      <h1>{label}</h1>
    </button>
  );
};