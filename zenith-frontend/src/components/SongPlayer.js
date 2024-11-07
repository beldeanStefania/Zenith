import React, { useState, useEffect, useRef } from 'react';

function SongPlayer({ songId }) {
  const [audioSrc, setAudioSrc] = useState(null);
  const audioRef = useRef(null); // referință pentru elementul audio

  useEffect(() => {
    const fetchAudio = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/song/play/${songId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'audio/mp3', // Sau "audio/mpeg" pentru fișiere MP3
          },
        });
        if (!response.ok) throw new Error('Failed to fetch audio');

        const blob = await response.blob();
        const url = URL.createObjectURL(blob);
        setAudioSrc(url);

        if (audioRef.current) {
          audioRef.current.load(); // Reîncărcăm sursa audio
          audioRef.current.play(); // Începem redarea
        }
      } catch (error) {
        console.error('Error fetching the audio file:', error);
      }
    };

    if (songId) {
      fetchAudio();
    }

    // Eliberează URL-ul creat anterior pentru a preveni scurgerile de memorie
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
          <source src={audioSrc} type="audio/mp3" /> {/* Sau "audio/mpeg" */}
          Your browser does not support the audio element.
        </audio>
      ) : (
        <p>Se încarcă audio...</p>
      )}
    </div>
  );
}

export default SongPlayer;
