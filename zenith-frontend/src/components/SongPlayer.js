import React, { useState, useEffect, useRef } from 'react';

function SongPlayer({ songId }) {
  const [audioSrc, setAudioSrc] = useState(null);
  const audioRef = useRef(null);

  useEffect(() => {
    const fetchAudio = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/song/play/${songId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'audio/mp3',
          },
        });
        if (!response.ok) throw new Error('Failed to fetch audio');

        const blob = await response.blob();
        const url = URL.createObjectURL(blob);
        setAudioSrc(url);

        if (audioRef.current) {
          audioRef.current.load();
          audioRef.current.play();
        }
      } catch (error) {
        console.error('Error fetching the audio file:', error);
      }
    };

    if (songId) {
      fetchAudio();
    }

    return () => {
      if (audioSrc) {
        URL.revokeObjectURL(audioSrc);
      }
    };
  }, [songId]);

  return (
    <div>
      {audioSrc ? (
        <audio controls autoPlay ref={audioRef}>
          <source src={audioSrc} type="audio/mp3" />
          Your browser does not support the audio element.
        </audio>
      ) : (
        <p>Loading audio...</p>
      )}
    </div>
  );
}

export default SongPlayer;
