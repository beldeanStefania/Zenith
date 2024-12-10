// StarLayer.tsx
import React from 'react';
import styled, { keyframes } from 'styled-components';

// Keyframe for star animation
const animStar = keyframes`
  from {
    transform: translateY(0px);
  }
  to {
    transform: translateY(-2000px);
  }
`;

// Type definition for props
interface StarLayerProps {
  size: number;
  duration: number;
  shadows: string;
}

// Styled component for each star layer
const StyledStarLayer = styled.div<StarLayerProps>`
  position: absolute;
  width: ${({ size }) => size}px;
  height: ${({ size }) => size}px;
  background: transparent;
  box-shadow: ${({ shadows }) => shadows};
  animation: ${animStar} ${({ duration }) => duration}s linear infinite;

  &::after {
    content: ' ';
    position: absolute;
    top: 2000px;
    width: ${({ size }) => size}px;
    height: ${({ size }) => size}px;
    background: transparent;
    box-shadow: ${({ shadows }) => shadows};
  }
`;

const StarLayer: React.FC<StarLayerProps> = ({ size, duration, shadows }) => (
  <StyledStarLayer size={size} duration={duration} shadows={shadows} />
);

export default StarLayer;
