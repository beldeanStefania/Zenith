import React, { useState } from 'react';
import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import './Profile.css';

const mockMoodData = [
  { date: '1 Dec', mood: 8 },
  { date: '2 Dec', mood: 6 },
  { date: '3 Dec', mood: 9 },
  { date: '4 Dec', mood: 7 },
  { date: '5 Dec', mood: 8 }
];

const CustomTooltip = ({ payload, label, active }: any) => {
  if (active && payload && payload.length) {
    return (
      <div style={{ background: 'rgba(0,0,0,0.8)', padding: '10px', borderRadius: '5px' }}>
        <p>{`Date: ${label}`}</p>
        <p style={{ color: 'var(--pink)' }}>{`Mood: ${payload[0].value}`}</p>
      </div>
    );
  }
  return null;
};

const Profile = () => {
  const [imagePreview, setImagePreview] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [profileData, setProfileData] = useState({
    username: localStorage.getItem('username') || 'Username',
    bio: '',
    favoriteGenres: ['Rock', 'Jazz', 'Electronic']
  });

  const handleSave = () => {
    setIsModalOpen(false);
  };

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setImagePreview(reader.result as string);
      };
      reader.readAsDataURL(file);
    }
  };

  return (
    <>
      {isModalOpen && (
        <>
          <div className="modal-overlay" onClick={() => setIsModalOpen(false)} />
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h2>Customize Profile</h2>
            <div className="form-field">
              <label>Username</label>
              <input 
                type="text" 
                value={profileData.username}
                onChange={e => setProfileData({...profileData, username: e.target.value})}
              />
            </div>
            <div className="form-field">
              <label>Bio</label>
              <input 
                type="text" 
                value={profileData.bio}
                onChange={e => setProfileData({...profileData, bio: e.target.value})}
                placeholder="Tell us about yourself..."
              />
            </div>
            <div className="form-field">
              <label>Favorite Genres</label>
              {profileData.favoriteGenres.map(genre => (
                <span key={genre} className="preference-tag">{genre}</span>
              ))}
              <span className="preference-tag">+ Add</span>
            </div>
            <button className="save-button" onClick={handleSave}>Save Changes</button>
          </div>
        </>
      )}

      <div className="profile-container">
        <div className="card">
          <div className="avatar">
            <div className="image-upload">
              <input type="file" onChange={handleImageUpload} accept="image/*" />
              <img src={imagePreview || "/api/placeholder/200/200"} alt="Profile" />
            </div>
          </div>
          <h2>{profileData.username}</h2>
          <p>Member since Dec 2024</p>
          <div className="customize-section">
            <span className="preference-tag" onClick={() => setIsModalOpen(true)}>Edit Profile</span>
          </div>
        </div>

        <div className="card">
          <h2>Your Music Profile</h2>
          <div className="mood-chart">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={mockMoodData}>
                <XAxis dataKey="date" stroke="var(--light-gray)" />
                <YAxis stroke="var(--light-gray)" />
                <Tooltip content={<CustomTooltip />} />
                <Line 
                  type="monotone" 
                  dataKey="mood" 
                  stroke="url(#colorGradient)" 
                  strokeWidth={2} 
                />
                <defs>
                  <linearGradient id="colorGradient" x1="0" y1="0" x2="1" y2="0">
                    <stop offset="0%" stopColor="var(--pink)" />
                    <stop offset="100%" stopColor="var(--light-blue)" />
                  </linearGradient>
                </defs>
              </LineChart>
            </ResponsiveContainer>
          </div>

          <div className="stats-grid">
            <div className="stat-card">
              <h3>32</h3>
              <p>Hours Listened</p>
            </div>
            <div className="stat-card">
              <h3>145</h3>
              <p>Songs Added</p>
            </div>
            <div className="stat-card">
              <h3>8</h3>
              <p>Playlists Created</p>
            </div>
          </div>

          <div className="recommendation-section">
            <h3>Based on your current mood</h3>
            <div className="recommend-card card">
              <img src="/api/placeholder/60/60" alt="Song" />
              <div>
                <h4>Chill Vibes Mix</h4>
                <p>Perfect for your relaxed mood</p>
              </div>
            </div>
          </div>

          <h3>Recent Playlists</h3>
          <div className="playlist-grid">
            {[
              { name: 'Chill Evening', songs: 12 },
              { name: 'Workout Mix', songs: 18 },
              { name: 'Focus Time', songs: 15 }
            ].map((playlist, index) => (
              <div 
                className="playlist-card" 
                key={playlist.name}
                style={{ animationDelay: `${index * 0.1}s` }}
              >
                <img src="/api/placeholder/200/200" alt={playlist.name} />
                <h4>{playlist.name}</h4>
                <p>{playlist.songs} songs</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </>
  );
};

export default Profile;