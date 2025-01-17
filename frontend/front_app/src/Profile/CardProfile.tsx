/**
 * @fileoverview CardProfile component that manages user profile picture display and upload
 * @requires react
 */

import React, { useEffect, useState } from "react";
import "./CardProfile.css";

/**
 * CardProfile component that provides profile picture management functionality
 * Handles image display and upload with local storage persistence
 * 
 * @component
 * @returns {JSX.Element} A profile picture upload interface with preview
 */
const CardProfile: React.FC = () => {
  /**
   * State to manage the profile picture URL
   * Initializes from localStorage or falls back to default image
   * @state
   */
  const [imagePreviewUrl, setImagePreviewUrl] = useState<string>(
    localStorage.getItem("profile_picture") || "d4741cb779ddec6509ca1ae0cb137a7d-removebg-preview.png"
  );

  /**
   * Handles the upload of a new profile picture
   * Converts uploaded file to base64 and stores in localStorage
   * 
   * @function
   * @param {React.ChangeEvent<HTMLInputElement>} e - The file input change event
   */
  const handlePhotoUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        const imageBase64 = reader.result as string;
        setImagePreviewUrl(imageBase64);
        localStorage.setItem("profile_picture", imageBase64);
      };
      reader.readAsDataURL(file);
    }
  };

  return (
    <div className="profile-picture-upload">
      {/* Profile picture upload label and input */}
      <label htmlFor="photo-upload" className="custom-file-upload fas">
        <div className="img-wrap">
          <img src={imagePreviewUrl} alt="Profile Preview" />
        </div>
        <input
          id="photo-upload"
          type="file"
          accept="image/*"
          onChange={handlePhotoUpload}
        />
      </label>
    </div>
  );
};

export default CardProfile;