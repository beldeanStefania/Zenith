import { useState } from "react";

import "./Toggle.css";

export const Toggle = ({
  label,
  toggled,
  onClick,
}: {
  label: string;
  toggled: boolean;
  onClick: Function;
}) => {
  const [isToggled, toggle] = useState(toggled);

  const callback = () => {
    toggle(!isToggled);
    onClick(!isToggled);
  };

  return (
    // <label>
    //   <input type="checkbox" defaultChecked={isToggled} onClick={callback} />
    //   <span />
    // </label>
    <button className="button" onClick={() => callback()}>
      <h1>{label}</h1>
    </button>
  );
};
