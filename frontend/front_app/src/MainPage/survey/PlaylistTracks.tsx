import axios from "axios";
import { useEffect, useState } from "react";

interface PlaylistTracksProps {
  username: string;
  playlistId: string | undefined;
}

const PlaylistTracks = ({ username, playlistId }: PlaylistTracksProps) => {
  interface Track {
    track: {
      name: string;
    };
  }

  const [tracks, setTracks] = useState<Track[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!playlistId) return;

    const fetchPlaylistTracks = async () => {
      try {
        const url = `http://localhost:8080/api/spotify/view-playlist?username=${username}&playlistId=${playlistId}`;
        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });

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
