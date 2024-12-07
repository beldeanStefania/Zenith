// Background.tsx
import styled from 'styled-components';

const Background = styled.div`
  height: 100vh;
  background: radial-gradient(ellipse at bottom, var(--star1) 0%, var(--star2) 100%);
  overflow: hidden;
  position: relative;
`;

export default Background;
