import React, { useEffect, useState } from "react";
import "./CardProfile.css";

interface CardProfileProps {
  username: string;
}

const CardProfile: React.FC<CardProfileProps> = ({ username }) => {
  // Cheia din localStorage devine profile_picture_<username>
  const keyForUser = `profile_picture_${username}`;

  // Citește imaginea, dacă există, altfel fallback
  const [imagePreviewUrl, setImagePreviewUrl] = useState<string>(() => {
    const stored = localStorage.getItem(keyForUser);
    return stored
      ? stored
      : "d4741cb779ddec6509ca1ae0cb137a7d-removebg-preview.png"; // fallback / default
  });

  // Când încărcăm un fișier, îl transformăm în base64 și-l salvăm în localStorage
  const handlePhotoUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        const imageBase64 = reader.result as string;
        setImagePreviewUrl(imageBase64);
        localStorage.setItem(keyForUser, imageBase64);
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
