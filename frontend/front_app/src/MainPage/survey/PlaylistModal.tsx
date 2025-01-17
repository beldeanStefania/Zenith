import React, { useState, useEffect } from "react";
import axios from "axios";
import ViewPlaylist from "./ViewPlaylist";

// PlaylistModal is a component that displays a modal with the songs of a playlist 
interface PlaylistModalProps {
  isOpen: boolean;
  onRequestClose: () => void;
  playlistName: string;
  mood: string; 
  playlistLink: string; 
  playlistSpotifyLink: string; 
}

const PlaylistModal: React.FC<PlaylistModalProps> = ({
  isOpen,
  onRequestClose,
  playlistName,
  playlistLink,
  mood,
}) => {
  const [songs, setSongs] = useState<any[]>([]); // Array of songs
  const [loading, setLoading] = useState<boolean>(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null); 
  const [successMessage, setSuccessMessage] = useState<string | null>(null); 

  // Funcție pentru a obține piesele playlistului
  const fetchPlaylistSongs = async () => {
    try {
      const response = await axios.get(playlistLink); 
      setSongs(response.data.tracks.items); 
      setLoading(false);
    } catch (error) {
      console.error("Error fetching playlist songs:", error);
      //setErrorMessage("Failed to load songs. Please try again.");
      setLoading(false);
    }
  };

  // Function to play a playlist
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

  // Function to save a playlist
  const handleSavePlaylist = async () => {
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("username"); 

    if (!token || !username) {
      setErrorMessage("You must be logged in to save a playlist.");
      return;
    }

    try {
      const url = `http://localhost:8080/api/playlists/add?username=${username}&name=${playlistName}&mood=${mood}&spotifyPlaylistId=${playlistLink}`;
      await axios.post(
        url,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setSuccessMessage("Playlist saved successfully!");
      if (window.location.pathname === "/profile") {
        window.location.reload();
      }
    } catch (error) {
      console.error("Error saving playlist:", error);
      setErrorMessage("Failed to save playlist. Please try again.");
    }
  };

  // Use useEffect to load the songs when the modal is opened
  useEffect(() => {
    if (isOpen) {
      fetchPlaylistSongs();
    }
  }, [isOpen]);

  const playlistId = playlistLink; // Obtain playlistId from playlistLink

  return (
    <ViewPlaylist
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      playlistName={playlistName}
      songs={songs}
      loading={loading}
      handlePlay={handlePlay}
      handleSavePlaylist={handleSavePlaylist}
      successMessage={successMessage}
      username={localStorage.getItem("username") || ""}
      playlistId={playlistId}
    />
  );
};

export default PlaylistModal;
