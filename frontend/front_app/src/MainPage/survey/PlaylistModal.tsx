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
  playlistLink, // Link-ul pentru playlist
}) => {
  const [songs, setSongs] = useState<any[]>([]); // Piesele playlistului
  const [loading, setLoading] = useState<boolean>(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null); // Mesaj de eroare

  // Funcție pentru a obține piesele playlistului
  const fetchPlaylistSongs = async () => {
    try {
      const response = await axios.get(playlistLink); // Aici folosești link-ul care conține piesele
      setSongs(response.data.tracks.items); // Presupunem că Spotify returnează piesele în acest format
    } catch (error) {
      console.error("Error fetching playlist songs:", error);
      setErrorMessage("Failed to load songs. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  // Folosește useEffect pentru a încărca piesele atunci când se deschide modalul
  useEffect(() => {
    if (isOpen) {
      fetchPlaylistSongs(); // Căutăm piesele din playlist când modalul este deschis
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
      ) : errorMessage ? (
        <p style={{ color: "red" }}>{errorMessage}</p> // Afișează eroarea dacă există
      ) : (
        <ul>
          {songs.length > 0 ? (
            songs.map((song: any, index: number) => (
              <li key={index}>
                {song.track.name} by {song.track.artists[0].name} {/* Afișează numele piesei și artistul */}
              </li>
            ))
          ) : (
            <p>No songs available in this playlist.</p>
          )}
        </ul>
      )}

      <button onClick={onRequestClose}>Close</button>
    </Modal>
  );
};

export default PlaylistModal;
