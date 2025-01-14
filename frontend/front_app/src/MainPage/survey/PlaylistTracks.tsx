/**
 * @fileoverview PlaylistTracks component that displays the tracks of a Spotify playlist
 * Fetches and renders track information from a specified playlist
 * @requires axios
 * @requires react
 */

import axios from "axios";
import { useEffect, useState } from "react";

interface PlaylistTracksProps {
  username: string;
  playlistId: string | undefined;
}


/**
 * PlaylistTracks component renders a list of tracks from a Spotify playlist
 * Handles loading states and error conditions during track fetching
 * 
 * @component
 * @param {Object} props - Component properties
 * @param {string} props.username - Username of the playlist owner
 * @param {string | undefined} props.playlistId - Spotify playlist identifier
 * @returns {JSX.Element} A list of tracks or appropriate loading/error states
 */
const PlaylistTracks = ({ username, playlistId }: PlaylistTracksProps) => {
  interface Track {
    track: {
      name: string;
    };    
  }

    /**
   * State management for track data and loading states
   */
  const [tracks, setTracks] = useState<Track[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

   /**
   * Effect hook to fetch playlist tracks when component mounts or playlistId changes
   * Handles API communication and error states
   */
  useEffect(() => {
    if (!playlistId) return;

    /**
     * Fetches playlist tracks from the API
     * Updates component state based on response
     * @async
     */
    const fetchPlaylistTracks = async () => {
      try {
        const url = `http://localhost:8080/api/spotify/view-playlist?username=${username}&playlistId=${playlistId}`;
        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });

        // Set tracks from response data
        // Presupunem că răspunsul conține un array de items care include obiectele de track-uri
        const data = response.data.tracks.items;
        setTracks(data);
      } catch (error: any) {
        console.error("Error fetching playlist tracks: ", error);
        setError("Failed to fetch playlist tracks. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchPlaylistTracks();
  }, [playlistId, username]);

  return (
    <div>
      {loading ? (
        <p>Loading...</p>
      ) : error ? (
        <p>{error}</p>
      ) : (
        <ul>
          {tracks.length > 0 ? (
            tracks.map((track, index) => (
              <li key={index}>{track.track.name}</li>
            ))
          ) : (
            <p>No tracks found</p>
          )}
        </ul>
      )}
    </div>
  );
};

export default PlaylistTracks;
