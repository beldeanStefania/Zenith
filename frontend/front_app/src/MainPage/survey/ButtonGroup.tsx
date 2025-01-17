import React from "react";
import "./ButtonGroup.css";

/**
 * ButtonGroup component renders a horizontal group of numbered buttons (1-5)
 * Used in surveys and rating interfaces to collect user input
 * Features gradient background and hover effects
 * 
 * @component
 * @param {Object} props - Component properties
 * @param {(answer: number) => void} props.onAnswer - Callback function triggered when a rating is selected
 * @returns {JSX.Element} A group of rating buttons arranged horizontally
 * 
 * @example
 * ```tsx
 * <ButtonGroup onAnswer={(rating) => console.log(`User selected: ${rating}`)} />
 * ```
 */
const ButtonGroup = ({ onAnswer }: { onAnswer: (answer: number) => void }) => {
  return (
    <div className="vertical-align">
      <div className="btns">
        {[1, 2, 3, 4, 5].map((value) => (
          <button 
            key={value} 
            className="btn" 
            onClick={() => onAnswer(value)}
          >
            {value}
          </button>
        ))}
      </div>
    </div>
  );
};

export default ButtonGroup;
