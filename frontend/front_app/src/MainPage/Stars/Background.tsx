/**
 * @fileoverview Background component providing a styled container with gradient background
 * @requires styled-components
 */

import styled from 'styled-components';

/**
 * Styled div component that creates a full-height gradient background
 * Uses CSS variables for theme-aware colors
 * Creates an elliptical radial gradient from bottom
 * Maintains overflow hidden to contain child animations
 * 
 * @component
 * @styled
 * @returns {StyledComponent} A styled div with gradient background
 * 
 * @example
 * ```tsx
 * <Background>
 *   <ChildContent />
 * </Background>
 * ```
 */
const Background = styled.div`
  height: 100vh;
  background: radial-gradient(ellipse at bottom, var(--star1) 0%, var(--star2) 100%);
  overflow: hidden;
  position: relative;
`;

export default Background;