/**
 * @fileoverview Profile component that displays user information and playlists
 * @requires react
 * @requires recharts
 * @requires axios
 */

import React, { useEffect, useState } from "react";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";
import "./Profile.css";
import CardProfile from "./CardProfile";
import Survey from "../MainPage/survey/Survey";
import axios from "axios";
import ViewPlaylist from "../MainPage/survey/ViewPlaylist";

/**
 * Interface for playlist data structure
 * @interface Playlist
 */
interface Playlist {
  id: number;
  name: string;
  createdAt: string;
  mood: string;
  spotifyPlaylistId: string;
}

/**
 * Custom tooltip component for the mood chart
 * @component
 * @param {Object} props - Tooltip props from recharts
 * @returns {JSX.Element|null} Rendered tooltip or null if inactive
 */
const CustomTooltip = ({ payload, label, active }: any) => {
  if (active && payload && payload.length) {
    return (
      <div
        style={{
          background: "rgba(0,0,0,0.8)",
          padding: "10px",
          borderRadius: "5px",
        }}
      >
        <p>{`Date: ${label}`}</p>
        <p style={{ color: "var(--pink)" }}>{`Mood: ${payload[0].value}`}</p>
      </div>
    );
  }
  return null;
};

/**
 * Profile component that displays user information, mood history, and playlists
 * Provides functionality to view and manage playlists
 * 
 * @component
 * @returns {JSX.Element} The complete user profile interface
 */
const Profile = () => {
  /**
   * State management for various profile features
   */
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [show, setShowSurvey] = useState(false);
  const [profileData, setProfileData] = useState({
    username: localStorage.getItem("username") || "Username",
    bio: "",
    favoriteGenres: ["Rock", "Jazz", "Electronic"],
  });
  
  const [playlists, setPlaylists] = useState<Playlist[]>([]);
  const [showPlaylist, setShowPlaylist] = useState(false);
  const [namePlaylists, setNamePlaylists] = useState("");
  const [idPlaylist, setIdPLaylist] = useState<number | null>(null);
  const [playlistSpotifyLink, setPlaylistSpotifyLink] = useState("");

  /**
   * Authentication token for API requests
   */
  const token = localStorage.getItem("token");

  /**
   * Handles saving profile changes
   * @function
   */
  const handleSave = () => {
    setIsModalOpen(false);
  };

  /**
   * Fetches user playlists from the server
   * @async
   * @function
   */
  const fetchPlaylists = async () => {
    try {
      const url = `http://localhost:8080/api/playlists/getPlaylists/${profileData.username}`;
      const response = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setPlaylists(response.data);
    } catch (error) {
      console.error("Error fetching playlists:", error);
    }
  };

  /**
   * Effect hook to fetch playlists on component mount
   */
  useEffect(() => {
    fetchPlaylists();
  }, []);

  /**
   * Transforms playlist data for mood chart
   */
  let mockMoodData: { date: string; mood: string }[] = [];

  try {
    mockMoodData = playlists.map((playlist) => ({
      date: playlist.createdAt,
      mood: playlist.mood,
    }));
  } catch (error) {
    console.log(error);
  }

  const cuteData = [{ number: playlists.length, name: "Playlists Created" }];

  return (
    <>
      {/* Profile customization modal */}
      {isModalOpen && (
        <>
          <div
            className="modal-overlay"
            onClick={() => setIsModalOpen(false)}
          />
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h2>Customize Profile</h2>
            <div className="form-field">
              <label>Username</label>
              <input
                type="text"
                value={profileData.username}
                onChange={(e) =>
                  setProfileData({ ...profileData, username: e.target.value })
                }
              />
            </div>
            <div className="form-field">
              <label>Bio</label>
              <input
                type="text"
                value={profileData.bio}
                onChange={(e) =>
                  setProfileData({ ...profileData, bio: e.target.value })
                }
                placeholder="Tell us about yourself..."
              />
            </div>
            <div className="form-field">
              <label>Favorite Genres</label>
              {profileData.favoriteGenres.map((genre) => (
                <span key={genre} className="preference-tag">
                  {genre}
                </span>
              ))}
              <span className="preference-tag">+ Add</span>
            </div>
            <button className="save-button" onClick={handleSave}>
              Save Changes
            </button>
          </div>
        </>
      )}

      {/* Main profile content */}
      <div className="profile-container">
        {/* Profile card section */}
        <div className="card">
          <CardProfile />
          <div className="bio">
            <h1>{profileData.username}</h1>
            <div className="customize-section">
              <span
                className="preference-tag"
                onClick={() => setIsModalOpen(true)}
              >
                Edit Profile
              </span>
              <Survey
                show={show}
                setShowSurvey={setShowSurvey}
                textButton={"Make a new playlist"}
                styleButton="preference-tag"
              />
            </div>
          </div>
        </div>

        {/* Music profile section */}
        <div className="card">
          <h2>Your Music Profile</h2>
          {playlists.length === 0 ? (
            <p>No playlists found</p>
          ) : (
            <div className="mood-chart">
              <ResponsiveContainer width="100%" height="105%">
                <LineChart data={mockMoodData}>
                  <XAxis dataKey="date" stroke="var(--light-gray)" />
                  <YAxis
                    dataKey="mood"
                    stroke="var(--light-gray)"
                    type="category"
                  />
                  <Tooltip content={<CustomTooltip />} />
                  <Line
                    type="monotone"
                    dataKey="mood"
                    stroke="url(#colorGradient)"
                    strokeWidth={2}
                  />
                  <defs>
                    <linearGradient
                      id="colorGradient"
                      x1="0"
                      y1="0"
                      x2="1"
                      y2="0"
                    >
                      <stop offset="0%" stopColor="var(--pink)" />
                      <stop offset="100%" stopColor="var(--light-blue)" />
                    </linearGradient>
                  </defs>
                </LineChart>
              </ResponsiveContainer>
            </div>
          )}

          {/* Stats section */}
          <div className="stats-grid">
            <div className="recommend-card card">
              <p className="card-h">{playlists.length}</p>
              <p className="card-p">Playlists Created</p>
            </div>
          </div>

          {/* Playlists section */}
          <div className="recommendation-section">
            <h3>Recent Playlists</h3>
            <div
              className="playlist-grid"
              onClick={() => setShowPlaylist(!showPlaylist)}
            >
              {playlists.length === 0 ? (
                <p>No playlists found</p>
              ) : (
                playlists.map((playlist) => (
                  <div
                    className="playlist-card"
                    key={playlist.id}
                    onClick={() => {
                      setNamePlaylists(playlist.name);
                      setIdPLaylist(playlist.id);
                      setPlaylistSpotifyLink(playlist.spotifyPlaylistId);
                    }}
                  >
                    <h4>{playlist.name}</h4>
                    <p>is {playlist.mood}</p>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Playlist viewer modal */}
      <ViewPlaylist
        isOpen={showPlaylist}
        onRequestClose={() => setShowPlaylist(!showPlaylist)}
        playlistName={namePlaylists}
        songs={[]}
        loading={false}
        handlePlay={() => {}}
        handleSavePlaylist={() => {}}
        successMessage=""
        username={profileData.username}
        playlistId={playlistSpotifyLink}
      />
    </>
  );
};

export default Profile;