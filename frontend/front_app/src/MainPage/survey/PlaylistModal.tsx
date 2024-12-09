import React, { useState, useEffect, useRef } from "react";
import Modal from "react-modal";
import axios from "axios";

interface PlaylistModalProps {
  isOpen: boolean;
  onRequestClose: () => void;
  playlistName: string;
}

interface Song {
  id: number;
  title: string;
  artist: string;
}

const PlaylistModal: React.FC<PlaylistModalProps> = ({
  isOpen,
  onRequestClose,
  playlistName,
}) => {
  const [songs, setSongs] = useState<Song[]>([]);
  const [currentAudio, setCurrentAudio] = useState<string | null>(null);
  const audioPlayerRef = useRef<HTMLAudioElement | null>(null);

  // Când modalul e deschis și avem un playlistName, încărcăm melodiile
  useEffect(() => {
    if (isOpen && playlistName) {
      fetchSongs();
    }
  }, [isOpen, playlistName]);

  // După ce se schimbă currentAudio, încercăm să redăm piesa
  useEffect(() => {
    if (audioPlayerRef.current && currentAudio) {
      audioPlayerRef.current.play().catch((err) =>
        console.error("Error playing the song:", err)
      );
    }
  }, [currentAudio]);

  const fetchSongs = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      console.error("User not logged in, cannot fetch songs.");
      return;
    }

    try {
      const response = await axios.get<Song[]>(
        `http://localhost:8080/api/userPlaylist/getSongsFromPlaylist/${playlistName}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log("Songs fetched:", response.data);
      setSongs(response.data);
    } catch (error) {
      console.error("Failed to fetch songs:", error);
    }
  };

  const handlePlaySong = async (songId: number) => {
    const token = localStorage.getItem("token");
    if (!token) {
      console.error("User not logged in, cannot play song.");
      return;
    }

    const audioUrl = `http://localhost:8080/api/userPlaylist/getAudio/${songId}`;
    const response = await axios.get(audioUrl, {
      headers: { Authorization: `Bearer ${token}` },
      responseType: "arraybuffer",
    });

    const blob = new Blob([response.data], { type: "audio/mpeg" });
    const blobUrl = URL.createObjectURL(blob);

    setCurrentAudio(blobUrl);
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="Playlist Modal"
      overlayClassName="customOverlay"
      className="customModal"
    >
      <h2>Playlist: {playlistName}</h2>

      {songs.length === 0 ? (
        <p>No songs available.</p>
      ) : (
        <ul>
          {songs.map((song) => (
            <li key={song.id}>
              {song.title} by {song.artist}
              <button onClick={() => handlePlaySong(song.id)}>Play</button>
            </li>
          ))}
        </ul>
      )}

{currentAudio && (
  <audio controls ref={audioPlayerRef} autoPlay key={currentAudio}>
    <source src={currentAudio} type="audio/mpeg" />
    Your browser does not support the audio tag.
  </audio>
)}


      <button onClick={onRequestClose}>Close</button>
    </Modal>
  );
};

export default PlaylistModal;
