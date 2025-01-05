import React, { useEffect } from 'react';

interface InitializationError {
  message: string;
}

interface AuthenticationError {
  message: string;
}

interface AccountError {
  message: string;
}

interface PlaybackError {
  message: string;
}

interface PlayerState {
  // Define the expected properties based on Spotify's documentation or your requirements
}

interface DeviceInfo {
  device_id: string;
}

interface SpotifyPlayerProps {
  accessToken: string;
}

const SpotifyPlayer: React.FC<SpotifyPlayerProps> = ({ accessToken }) => {
  useEffect(() => {
    if (window.Spotify) {
      const player = new window.Spotify.Player({
        name: 'Web Playback SDK Quick Start Player',
        getOAuthToken: (cb: (token: string) => void) => { cb(accessToken); }, // Correctly typed callback
        volume: 0.5
      });

      // Attach listeners
      player.addListener('initialization_error', ({ message }: InitializationError) => { console.error(message); });
      player.addListener('authentication_error', ({ message }: AuthenticationError) => { console.error(message); });
      player.addListener('account_error', ({ message }: AccountError) => { console.error(message); });
      player.addListener('playback_error', ({ message }: PlaybackError) => { console.error(message); });

      player.addListener('player_state_changed', (state: PlayerState) => { console.log(state); });

      player.addListener('ready', ({ device_id }: DeviceInfo) => {
        console.log('Ready with Device ID', device_id);
      });

      player.addListener('not_ready', ({ device_id }: DeviceInfo) => {
        console.log('Device ID has gone offline', device_id);
      });

      // Connect to the player
      player.connect();

      // Cleanup on unmount
      return () => {
        player.disconnect();
      };
    }
  }, [accessToken]); // Re-run when accessToken changes

  return (
    <div>
      <p>Spotify Player will appear here</p>
    </div>
  );
};

export default SpotifyPlayer;

