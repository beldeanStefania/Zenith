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

const Profile = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [show, setShowSurvey] = useState(false);
  const [profileData, setProfileData] = useState({
    username: localStorage.getItem("username") || "Username",
    bio: localStorage.getItem("bio") || "",
    favoriteGenres: JSON.parse(
      localStorage.getItem("favoriteGenres") || '["Rock", "Jazz", "Electronic"]'
    ),
  });
  const token = localStorage.getItem("token");
  const [newGenre, setNewGenre] = useState("");
  const [tempGenres, setTempGenres] = useState(profileData.favoriteGenres);
  const [isAddingGenre, setIsAddingGenre] = useState(false);

  interface Playlist {
    id: number;
    name: string;
    createdAt: string;
    mood: string;
    spotifyPlaylistId: string;
  }

  const [playlists, setPlaylists] = useState<Playlist[]>([]);
  const [showPlaylist, setShowPlaylist] = useState(false);
  const [namePlaylists, setNamePlaylists] = useState("");
  const [idPlaylist, setIdPLaylist] = useState<number | null>(null);
  const [playlistSpotifyLink, setPlaylistSpotifyLink] = useState("");
  const [bio, setBio] = useState(profileData.bio);

  const debounce = (func: Function, delay: number) => {
    let timeout: NodeJS.Timeout;
    return (...args: any[]) => {
      clearTimeout(timeout);
      timeout = setTimeout(() => func(...args), delay);
    };
  };

  const handleSave = () => {
    setIsAddingGenre(false);
    setIsModalOpen(false);
    localStorage.setItem("bio", bio);
    localStorage.setItem("favoriteGenres", JSON.stringify(tempGenres));
    setProfileData({ ...profileData, bio, favoriteGenres: tempGenres });
  };

  const handleDebouncedSave = debounce(() => {
    localStorage.setItem("bio", bio);
  }, 1000);

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

  const handleAddGenre = () => {
    if (newGenre.trim() && !tempGenres.includes(newGenre)) {
      setTempGenres([...tempGenres, newGenre.trim()]);
      setNewGenre("");
    }
  };

  const handleRemoveGenre = (genre: string) => {
    setTempGenres(tempGenres.filter((g: string) => g !== genre));
  };

  const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      handleAddGenre();
    }
  };

  const toggleAddGenre = () => {
    setIsAddingGenre(true);
  };

  useEffect(() => {
    fetchPlaylists();
  }, []);

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
                placeholder="Tell us about yourself..."
                value={bio}
                onChange={(e) => {
                  setBio(e.target.value);
                  handleDebouncedSave();
                }}
                onBlur={handleSave}
              />
            </div>
            <div className="form-field">
              <label>Favorite Genres</label>
              {tempGenres.map(
                (
                  genre:
                    | boolean
                    | React.ReactElement<
                        any,
                        string | React.JSXElementConstructor<any>
                      >
                    | Iterable<React.ReactNode>
                    | React.Key
                    | null
                    | undefined
                ) => (
                  <span
                    key={genre}
                    className="preference-tag"
                    style={{ display: "inline-flex", alignItems: "center" }}
                    onMouseEnter={(e) =>
                      (e.currentTarget.querySelector(
                        ".remove-genre-button"
                      )!.style.visibility = "visible")
                    }
                    onMouseLeave={(e) =>
                      (e.currentTarget.querySelector(
                        ".remove-genre-button"
                      )!.style.visibility = "hidden")
                    }
                  >
                    {genre}
                    <span
                      className="remove-genre-button"
                      onClick={() => handleRemoveGenre(genre)}
                      style={{
                        marginLeft: "8px",
                        fontSize: "14px",
                        cursor: "pointer",
                        visibility: "hidden",
                      }}
                    >
                      ✕
                    </span>
                  </span>
                )
              )}
              {isAddingGenre ? (
                <div
                  className="add-genre-section"
                  style={{ display: "inline-flex", alignItems: "center" }}
                >
                  <input
                    type="text"
                    placeholder="Add a genre..."
                    value={newGenre}
                    onChange={(e) => setNewGenre(e.target.value)}
                    onKeyPress={handleKeyPress}
                    className="preference-tag-input"
                  />
                  <button
                    className="preference-tag"
                    onClick={() => setIsAddingGenre(false)}
                  >
                    Done
                  </button>
                </div>
              ) : (
                <span className="preference-tag" onClick={toggleAddGenre}>
                  + Add
                </span>
              )}
            </div>
            <button className="save-button" onClick={handleSave}>
              Save Changes
            </button>
          </div>
        </>
      )}
      <div className="profile-container">
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
              {bio ? <p className="bio-p">{bio}</p> : <p></p>}
              {profileData.favoriteGenres
                .filter(
                  (genre: any): genre is string => typeof genre === "string"
                )
                .map((genre: string) => (
                  <span key={genre} className="preference-tag-profil">
                    {genre}
                  </span>
                ))}
            </div>
          </div>
        </div>

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

          <div className="stats-grid">
            <div className="recommend-card card">
              <p className="card-h">{playlists.length}</p>
              <p className="card-p">Playlists Created</p>
            </div>
          </div>

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
