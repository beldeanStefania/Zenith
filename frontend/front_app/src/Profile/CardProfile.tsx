import React, { useEffect, useState } from "react";
import "./CardProfile.css";

const CardProfile: React.FC = () => {
  const [imagePreviewUrl, setImagePreviewUrl] = useState<string>(
    localStorage.getItem("profile_picture") ||
      "d4741cb779ddec6509ca1ae0cb137a7d-removebg-preview.png"
  );

  // Salvăm imaginea în localStorage
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
