/**
 * @fileoverview PlaylistModal component that displays playlist details and controls
 * Handles playlist playback, saving, and tracks display
 * @requires react
 * @requires axios
 */
import React, { useState, useEffect } from "react";
import axios from "axios";
import ViewPlaylist from "./ViewPlaylist";

// PlaylistModal va primi piesele playlistului prin props
interface PlaylistModalProps {
  isOpen: boolean;
  onRequestClose: () => void;
  playlistName: string;
  mood: string; // Adăugăm mood-ul playlistului
  playlistLink: string; // Adăugăm link-ul pentru playlist
  playlistSpotifyLink: string; // Adăugăm link-ul Spotify pentru playlist
}

/**
 * PlaylistModal component provides a modal interface for playlist interaction
 * Manages playlist playback, saving functionality, and track display
 * 
 * @component
 * @param {Object} props - Component properties
 * @param {boolean} props.isOpen - Controls modal visibility
 * @param {Function} props.onRequestClose - Handler for closing the modal
 * @param {string} props.playlistName - Name of the playlist
 * @param {string} props.playlistLink - Link to the playlist
 * @param {string} props.mood - Mood associated with the playlist
 * @param {string} props.playlistSpotifyLink - Spotify URL for the playlist
 * @returns {JSX.Element} Modal with playlist information and controls
 */
const PlaylistModal: React.FC<PlaylistModalProps> = ({
  isOpen,
  onRequestClose,
  playlistName,
  playlistLink,
  mood,
}) => {
  /**
   * State management for playlist data and UI states
   */
  const [songs, setSongs] = useState<any[]>([]); // Piesele playlistului
  const [loading, setLoading] = useState<boolean>(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null); // Mesaj de eroare
  const [successMessage, setSuccessMessage] = useState<string | null>(null); // Mesaj de succes

   /**
   * Fetches songs for the playlist from the API
   * @async
   */
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

  /**
   * Handles playlist playback through Spotify
   * @async
   */
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

  /**
   * Handles saving the playlist to user's library
   * @async
   */
  const handleSavePlaylist = async () => {
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("username"); // asigură-te că acesta este salvat în localStorage sau gestionat corespunzător

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

  /**
   * Effect hook to fetch playlist songs when modal opens
   */
  useEffect(() => {
    if (isOpen) {
      fetchPlaylistSongs();
    }
  }, [isOpen]);

  const playlistId = playlistLink; // Obține id-ul playlistului din link

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
