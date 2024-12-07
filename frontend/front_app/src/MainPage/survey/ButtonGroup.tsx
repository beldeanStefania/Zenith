import React from "react";
import "./ButtonGroup.css";

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
