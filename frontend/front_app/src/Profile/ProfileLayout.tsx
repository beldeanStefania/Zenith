/**
 * @fileoverview ProfileLayout component that provides the basic structure for user profile displays
 * @requires react
 * @requires styled-components
 */

import React from 'react';
import styled from 'styled-components';

/**
 * Styled container component for the profile layout
 * Centers content and provides consistent padding
 */
const ProfileContainer = styled.div`
 display: flex;
 flex-direction: column;
 align-items: center;
 padding: 2rem;
 color: var(--light-gray);
`;

/**
 * Styled header component for profile information
 * Arranges avatar and user info horizontally
 */
const ProfileHeader = styled.div`
 display: flex;
 align-items: center;
 gap: 2rem;
 margin-bottom: 2rem;
`;

/**
 * Styled avatar component with gradient border effect
 * Handles image containment and border radius
 */
const Avatar = styled.div`
 width: 150px;
 height: 150px;
 border-radius: 50%;
 background: linear-gradient(90deg, var(--pink), var(--light-blue));
 padding: 3px;
 
 img {
   width: 100%;
   height: 100%;
   border-radius: 50%;
   object-fit: cover;
 }
`;

/**
 * Styled component for user information display
 * Handles text styling and spacing
 */
const UserInfo = styled.div`
 h1 {
   font-size: 2rem;
   margin-bottom: 0.5rem;
 }
 
 p {
   color: var(--light-purple);
 }
`;

/**
 * ProfileLayout component that provides a consistent layout structure for user profiles
 * Includes an avatar, username, and join date display
 * 
 * @component
 * @returns {JSX.Element} A styled profile layout template
 * 
 * @example
 * ```tsx
 * <ProfileLayout>
 *   {--Additional profile content --}
 * </ProfileLayout>
 * ```
 */

export const ProfileLayout: React.FC = () => {
 return (
   <ProfileContainer>
     <ProfileHeader>
       <Avatar>
         <img src="/api/placeholder/150/150" alt="User Avatar" />
       </Avatar>
       <UserInfo>
         <h1>Username</h1>
         <p>Joined December 2024</p>
       </UserInfo>
     </ProfileHeader>
   </ProfileContainer>
 );
};