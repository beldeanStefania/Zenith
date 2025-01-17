/**
 * @fileoverview StarLayer component that creates an animated star field effect
 * @requires react
 * @requires styled-components
 */

import React from 'react';
import styled, { keyframes } from 'styled-components';

/**
 * Keyframe animation for vertical star movement
 * Creates an infinite upward scrolling effect for stars
 * 
 * @constant
 * @type {Keyframes}
 */
const animStar = keyframes`
  from {
    transform: translateY(0px);
  }
  to {
    transform: translateY(-2000px);
  }
`;

/**
 * Interface defining the props for StarLayer component
 * @interface StarLayerProps
 * @property {number} size - Size of individual stars in pixels
 * @property {number} duration - Animation duration in seconds
 * @property {string} shadows - Box-shadow CSS string for creating star effect
 */
interface StarLayerProps {
  size: number;
  duration: number;
  shadows: string;
}

/**
 * Styled component that creates the animated star layer
 * Uses props to determine star size, animation duration, and star pattern
 * Creates a duplicate star field after the main field for seamless scrolling
 * 
 * @styled
 * @param {StarLayerProps} props - Component styling props
 */
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

/**
 * StarLayer component that renders an animated layer of stars
 * Can be stacked with different sizes and speeds for parallax effect
 * 
 * @component
 * @param {StarLayerProps} props - Component props
 * @returns {JSX.Element} An animated star layer
 * 
 * @example
 * ```tsx
 * <StarLayer 
 *   size={1} 
 *   duration={50} 
 *   shadows={generateBoxShadows(700)} 
 * />
 * ```
 */
const StarLayer: React.FC<StarLayerProps> = ({ size, duration, shadows }) => (
  <StyledStarLayer size={size} duration={duration} shadows={shadows} />
);

export default StarLayer;