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

// Tooltip-ul personalizat pentru graficul de mood
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
  // -------------------------------------------------------
  // Pregătire date user
  // -------------------------------------------------------
  // Aici extragem "username" din localStorage (salvat la login).
  const usernameFromLocal = localStorage.getItem("username") || "Username";

  // -------------------------------------------------------
  // Stare pentru modal Edit Profile
  // -------------------------------------------------------
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [showSurvey, setShowSurvey] = useState(false);
  const [isAddingGenre, setIsAddingGenre] = useState(false);
  const [newGenre, setNewGenre] = useState("");

  // -------------------------------------------------------
  // Bio, Genuri & Avatar per-user
  // -------------------------------------------------------
  // Chei: `bio_username`, `favoriteGenres_username`, `avatar_username`.
  const [bio, setBio] = useState(() => {
    const storedBio = localStorage.getItem(`bio_${usernameFromLocal}`);
    return storedBio ? storedBio : "";
  });

  const [tempGenres, setTempGenres] = useState(() => {
    const storedGenres = localStorage.getItem(`favoriteGenres_${usernameFromLocal}`);
    return storedGenres ? JSON.parse(storedGenres) : ["Rock", "Jazz", "Electronic"];
  });

  // Fie un link direct la imagine, fie un base64 (după preferință).
  const [avatarUrl, setAvatarUrl] = useState(() => {
    const storedAvatar = localStorage.getItem(`avatar_${usernameFromLocal}`);
    return storedAvatar ? storedAvatar : ""; // fallback
  });

  // Putem combina info într-un profileData, dar e ok să avem și separați avatarUrl, bio etc.
  const [profileData, setProfileData] = useState({
    username: usernameFromLocal,
    bio: bio,
    favoriteGenres: tempGenres,
    avatar: avatarUrl,
  });

  // -------------------------------------------------------
  // Funcții pentru adăugare / ștergere genuri
  // -------------------------------------------------------
  const handleAddGenre = () => {
    if (newGenre.trim() && !tempGenres.includes(newGenre.trim())) {
      setTempGenres([...tempGenres, newGenre.trim()]);
      setNewGenre("");
    }
  };

  const handleRemoveGenre = (genre: string) => {
    setTempGenres(tempGenres.filter((g) => g !== genre));
  };

  const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      handleAddGenre();
    }
  };

  const toggleAddGenre = () => {
    setIsAddingGenre(true);
  };

  // -------------------------------------------------------
  // Salvare (Edit Profile Modal)
  // -------------------------------------------------------
  const handleSave = () => {
    setIsAddingGenre(false);
    setIsModalOpen(false);

    // Salvăm tot în localStorage cu prefixul username
    localStorage.setItem(`bio_${profileData.username}`, bio);
    localStorage.setItem(
      `favoriteGenres_${profileData.username}`,
      JSON.stringify(tempGenres)
    );
    localStorage.setItem(`avatar_${profileData.username}`, avatarUrl);

    // Actualizăm state-ul principal
    setProfileData({
      ...profileData,
      bio: bio,
      favoriteGenres: tempGenres,
      avatar: avatarUrl,
    });
  };

  // -------------------------------------------------------
  // Playlists: fetch, view, play
  // -------------------------------------------------------
  const token = localStorage.getItem("token");

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
  const [playlistSpotifyLink, setPlaylistSpotifyLink] = useState("");

  const fetchPlaylists = async () => {
    try {
      // folosim profileData.username pentru a încărca playlist-urile corecte
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

  useEffect(() => {
    fetchPlaylists();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [profileData.username]);

  // Date pentru Recharts
  let mockMoodData: { date: string; mood: string }[] = [];
  try {
    mockMoodData = playlists.map((playlist) => ({
      date: playlist.createdAt,
      mood: playlist.mood,
    }));
  } catch (error) {
    console.log(error);
  }

  // Funcție pentru redarea playlist-ului (trimitere la backend)
  const handlePlay = async () => {
    const username = profileData.username; // username actual
    if (!token || !username) {
      console.error("User must be logged in to play a playlist.");
      return;
    }

    // Curățăm playlistId dacă conține "playlist/"
    const cleanPlaylistId = playlistSpotifyLink.includes("playlist/")
      ? playlistSpotifyLink.split("/playlist/")[1]
      : playlistSpotifyLink;

    try {
      const url = `http://localhost:8080/api/spotify/play-playlist?username=${username}&playlistId=${cleanPlaylistId}`;
      await axios.post(url, null, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log("DEBUG - Playlist is now playing!");
    } catch (error) {
      console.error("Error playing playlist:", error);
    }
  };

  // -------------------------------------------------------
  // RENDER
  // -------------------------------------------------------
  return (
    <>
      {/* ---------------------- MODAL PENTRU EDITARE PROFIL ---------------------- */}
      {isModalOpen && (
        <>
          <div className="modal-overlay" onClick={() => setIsModalOpen(false)} />
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h2>Customize Profile</h2>

            <div className="form-field">
              <label>Username</label>
              <input
                type="text"
                value={profileData.username}
                onChange={(e) =>
                  setProfileData({
                    ...profileData,
                    username: e.target.value,
                  })
                }
              />
            </div>

            <div className="form-field">
              <label>Bio</label>
              <input
                type="text"
                placeholder="Tell us about yourself..."
                value={bio}
                onChange={(e) => setBio(e.target.value)}
              />
            </div>

            <div className="form-field">
              <label>Favorite Genres</label>
              {tempGenres.map((genre) => (
                <span
                  key={genre}
                  className="preference-tag"
                  style={{ display: "inline-flex", alignItems: "center" }}
                  onMouseEnter={(ev) =>
                    (ev.currentTarget.querySelector(
                      ".remove-genre-button"
                    )!.style.visibility = "visible")
                  }
                  onMouseLeave={(ev) =>
                    (ev.currentTarget.querySelector(
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
              ))}
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

            {/* ---------------------------------------------------------
                Câmp nou pentru editarea Avatarului
                Poate fi un URL. Pentru fișiere, vezi explicațiile
               --------------------------------------------------------- */}
            <div className="form-field">
              <label>Avatar (URL)</label>
              <input
                type="text"
                placeholder="Paste an image link..."
                value={avatarUrl}
                onChange={(e) => setAvatarUrl(e.target.value)}
              />
            </div>

            <button className="save-button" onClick={handleSave}>
              Save Changes
            </button>
          </div>
        </>
      )}

      {/* ---------------------- MAIN PROFILE VIEW ---------------------- */}
      <div className="profile-container">
        <div className="card">
          {/* Trecem un prop la CardProfile, ca să știe ce avatar să afișeze */}
          <CardProfile username={profileData.username} />

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
                show={showSurvey}
                setShowSurvey={setShowSurvey}
                textButton="Make a new playlist"
                styleButton="preference-tag"
              />
              {bio && <p className="bio-p">{bio}</p>}
              <div className="genre-list">
                {profileData.favoriteGenres.map((genre) => (
                  <span key={genre} className="preference-tag-profil">
                    {genre}
                  </span>
                ))}
              </div>
            </div>
          </div>
        </div>

        <div className="card">
          <h2>Your Music Profile</h2>
          <div className="mood-chart">
            <ResponsiveContainer width="100%" height="100%">
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
                  stroke="var(--pink)"
                  strokeWidth={2}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>

          <h3>Recent Playlists</h3>
          <div className="playlist-grid">
            {playlists.map((playlist) => (
              <div
                className="playlist-card"
                key={playlist.id}
                onClick={() => {
                  setNamePlaylists(playlist.name);
                  // curățăm spotifyPlaylistId dacă e un URL
                  const cleanPlaylistId = playlist.spotifyPlaylistId.includes("playlist/")
                    ? playlist.spotifyPlaylistId.split("/playlist/")[1]
                    : playlist.spotifyPlaylistId;
                  setPlaylistSpotifyLink(cleanPlaylistId);
                  setShowPlaylist(true);
                }}
              >
                <h4>{playlist.name}</h4>
                <p>Mood: {playlist.mood}</p>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* ---------------------- VIEW PLAYLIST MODAL ---------------------- */}
      <ViewPlaylist
        isOpen={showPlaylist}
        onRequestClose={() => setShowPlaylist(false)}
        playlistName={namePlaylists}
        songs={[]}  // Poți trimite aici piesele, dacă le ai
        loading={false}
        handlePlay={handlePlay}
        handleSavePlaylist={() => {}}
        successMessage=""
        username={profileData.username}
        playlistId={playlistSpotifyLink}
      />
    </>
  );
};

export default Profile;
