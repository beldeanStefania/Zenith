import React, { useEffect } from "react";

/**
 * @fileoverview SpotifyPlayer component for integrating the Spotify Web Playback SDK
 * Handles player initialization, authentication, and playback events
 * @requires react
 */

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

/**
 * SpotifyPlayer component integrates the Spotify Web Playback SDK into the application
 * Initializes the player, handles authentication, and listens for playback events
 * 
 * @component
 * @param {Object} props - Component properties
 * @param {string} props.accessToken - Spotify access token for authentication
 * @returns {JSX.Element} Renders the Spotify player container
 */
const SpotifyPlayer: React.FC<SpotifyPlayerProps> = ({ accessToken }) => {

  /**
   * Effect hook to initialize the Spotify Web Playback SDK when the component mounts
   * Handles player setup, authentication, and event listeners
   */
  useEffect(() => {
    if (window.Spotify) {
      const player = new window.Spotify.Player({
        name: "Web Playback SDK Quick Start Player",
        getOAuthToken: (cb: (token: string) => void) => {
          cb(accessToken);
        }, // Correctly typed callback
        volume: 0.5,
      });

      // Attach listeners
      player.addListener(
        "initialization_error",
        ({ message }: InitializationError) => {
          console.error(message);
        }
      );

      player.addListener(
        "authentication_error",
        ({ message }: AuthenticationError) => {
          console.error(message);
        }
      );

      player.addListener("account_error", ({ message }: AccountError) => {
        console.error(message);
      });

      player.addListener("playback_error", ({ message }: PlaybackError) => {
        console.error(message);
      });

      player.addListener("player_state_changed", (state: PlayerState) => {
        console.log(state);
      });

      player.addListener("ready", ({ device_id }: DeviceInfo) => {
        console.log("Ready with Device ID", device_id);
      });

      player.addListener("not_ready", ({ device_id }: DeviceInfo) => {
        console.log("Device ID has gone offline", device_id);
      });

      // Connect to the player
      player.connect();

      /**
       * Cleanup function to disconnect the player when component unmounts
       */
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