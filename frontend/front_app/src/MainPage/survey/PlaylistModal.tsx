import React, { useState, useEffect, useRef } from "react";
import Modal from "react-modal";
import axios from "axios";

const PlaylistModal = ({
  isOpen,
  onRequestClose,
  playlistName,
}: {
  isOpen: boolean;
  onRequestClose: () => void;
  playlistName: string;
}) => {
  const [songs, setSongs] = useState<
    { id: number; title: string; artist: string }[]
  >([]);
  const [currentAudio, setCurrentAudio] = useState<string | null>(null);
  const audioPlayerRef = useRef<HTMLAudioElement | null>(null); // Referință pentru player-ul audio

  useEffect(() => {
    if (isOpen && playlistName) {
      fetchSongs();
    }
  }, [isOpen, playlistName]);

  const fetchSongs = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/userPlaylist/getSongsFromPlaylist/${playlistName}`
      );
      console.log("Songs fetched:", response.data);
      setSongs(response.data);
    } catch (error) {
      console.error("Failed to fetch songs:", error);
    }
  };

  const handlePlaySong = (songId: number) => {
    const audioUrl = `http://localhost:8080/api/userPlaylist/getAudio/${songId}`;
    console.log("Fetching audio from:", audioUrl);
    setCurrentAudio(audioUrl); 

    if (audioPlayerRef.current) {
      audioPlayerRef.current.pause();
      audioPlayerRef.current.load();
      audioPlayerRef.current.play().catch((err) => {
        console.error("Error playing the song:", err);
      });
    }
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
        <audio controls ref={audioPlayerRef} autoPlay>
          <source src={currentAudio} type="audio/mpeg" />
          Your browser does not support the audio tag.
        </audio>
      )}

      <button onClick={onRequestClose}>Close</button>
    </Modal>
  );
};

export default PlaylistModal;
