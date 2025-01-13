import React from 'react';
import styled from 'styled-components';

const ProfileContainer = styled.div`
 display: flex;
 flex-direction: column;
 align-items: center;
 padding: 2rem;
 color: var(--light-gray);
`;

const ProfileHeader = styled.div`
 display: flex;
 align-items: center;
 gap: 2rem;
 margin-bottom: 2rem;
`;

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

const UserInfo = styled.div`
 h1 {
   font-size: 2rem;
   margin-bottom: 0.5rem;
 }
 
 p {
   color: var(--light-purple);
 }
`;

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