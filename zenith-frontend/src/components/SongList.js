import React, { useState, useEffect } from 'react';
import SongPlayer from './SongPlayer';

function SongList() {
  const [songs, setSongs] = useState([]);
  const [selectedSongId, setSelectedSongId] = useState(null);

  useEffect(() => {
    const fetchSongs = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/song/getAll'); // Obține lista de melodii
        if (!response.ok) throw new Error('Failed to fetch songs');
        const data = await response.json();
        setSongs(data);
      } catch (error) {
        console.error('Error fetching songs:', error);
      }
    };

    fetchSongs();
  }, []);

  return (
    <div>
      <h1>Lista de Melodii</h1>
      <ul>
        {songs.map((song, index) => (
          <li key={index}>
            <button onClick={() => setSelectedSongId(song.id)}>
              {song.title} - {song.artist}
            </button>
          </li>
        ))}
      </ul>

      {selectedSongId && <SongPlayer songId={selectedSongId} />}
    </div>
  );
}

export default SongList;
