import React, { useState, useEffect } from "react";
import Modal from "react-modal";
import axios from "axios";

// PlaylistModal va primi piesele playlistului prin props
interface PlaylistModalProps {
  isOpen: boolean;
  onRequestClose: () => void;
  playlistName: string;
  playlistLink: string; // Adăugăm link-ul pentru playlist
}

const PlaylistModal: React.FC<PlaylistModalProps> = ({
  isOpen,
  onRequestClose,
  playlistName,
  playlistLink,
}) => {
  const [songs, setSongs] = useState<any[]>([]); // Piesele playlistului
  const [loading, setLoading] = useState<boolean>(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null); // Mesaj de eroare
  const [successMessage, setSuccessMessage] = useState<string | null>(null); // Mesaj de succes

  // Funcție pentru a obține piesele playlistului
  const fetchPlaylistSongs = async () => {
    try {
      const response = await axios.get(playlistLink); // Aici folosești link-ul care conține piesele
      setSongs(response.data.tracks.items); // Presupunem că Spotify returnează piesele în acest format
      setLoading(false);
    } catch (error) {
      console.error("Error fetching playlist songs:", error);
      //setErrorMessage("Failed to load songs. Please try again.");
      setLoading(false);
    }
  };

  // Funcție de redare a playlistului
  const handlePlay = async () => {
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("username");

    if (!token || !username) {
      setErrorMessage("You must be logged in to play a playlist.");
      return;
    }

    try {
      const url = `http://localhost:8080/api/spotify/play-playlist?username=${username}&playlistId=${playlistLink}`;
      await axios.post(url, null, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setSuccessMessage("Playlist is now playing!");
    } catch (error) {
      console.error("Error playing playlist:", error);
      setErrorMessage("Failed to play playlist. Please try again.");
    }
  };

  // Funcție de salvare a playlistului
  const handleSavePlaylist = async () => {
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("username"); // asigură-te că acesta este salvat în localStorage sau gestionat corespunzător

    if (!token || !username) {
        setErrorMessage("You must be logged in to save a playlist.");
        return;
    }

    try {
        const url = `http://localhost:8080/api/playlists/add?username=${username}&name=${playlistName}&spotifyPlaylistId=${playlistLink}`;
        await axios.post(url, {}, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        setSuccessMessage("Playlist saved successfully!");
    } catch (error) {
        console.error("Error saving playlist:", error);
        setErrorMessage("Failed to save playlist. Please try again.");
    }
};


  // Folosește useEffect pentru a încărca piesele atunci când se deschide modalul
  useEffect(() => {
    if (isOpen) {
      fetchPlaylistSongs();
    }
  }, [isOpen]);

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="Playlist Modal"
      overlayClassName="customOverlay"
      className="customModal"
    >
      <h2>{playlistName}</h2>

      {loading ? (
        <p>Loading...</p>
      ) : (
        <div>
          <ul>
            {songs.length > 0 ? (
              songs.map((song, index) => (
                <li key={index}>
                  {song.track.name} by {song.track.artists[0].name}
                  <audio controls>
                    <source src={song.track.preview_url} />
                  </audio>
                </li>
              ))
            ) : (
              <p></p>
            )}
          </ul>
        </div>
      )}

      {/* Butoane de control */}
      <div>
        <button onClick={handlePlay}>Play Playlist</button>
        <button onClick={handleSavePlaylist}>Save Playlist</button> {/* Butonul de salvare */}
        <button onClick={onRequestClose}>Close</button>
      </div>

      {successMessage && <p style={{ color: "green" }}>{successMessage}</p>}
    </Modal>
  );
};

export default PlaylistModal;
