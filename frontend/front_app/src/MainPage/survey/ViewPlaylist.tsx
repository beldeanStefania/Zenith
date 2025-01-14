import Modal from "react-modal";
import PlaylistTracks from "./PlaylistTracks";

/**
 * @fileoverview ViewPlaylist component for displaying a generated playlist
 * Shows the playlist name, tracks, and provides options to play and save the playlist
 * @requires react-modal
 * @requires ./PlaylistTracks
 */

interface ViewPlaylistProps {
  isOpen: boolean;
  onRequestClose: () => void;
  playlistName: string;
  songs: {
    track: { name: string; artists: { name: string }[]; preview_url: string };
  }[];
  loading: boolean;
  handlePlay: () => void;
  handleSavePlaylist: () => void;
  successMessage: string | null;
  username: string;
  playlistId: string | undefined;
}

/**
 * ViewPlaylist component displays a generated playlist with its name, tracks, and control options
 * Allows the user to play the playlist, save it, and view the tracks
 *
 * @component
 * @param {Object} props - Component properties
 * @param {boolean} props.isOpen - Flag to control the visibility of the playlist modal
 * @param {Function} props.onRequestClose - Function to close the playlist modal
 * @param {string} props.playlistName - Name of the generated playlist
 * @param {Object[]} props.songs - Array of song objects containing track details
 * @param {boolean} props.loading - Flag indicating if the playlist is being loaded
 * @param {Function} props.handlePlay - Function to handle playing the playlist
 * @param {Function} props.handleSavePlaylist - Function to handle saving the playlist
 * @param {string|null} props.successMessage - Success message to display after saving the playlist
 * @param {string} props.username - Username of the user
 * @param {string|undefined} props.playlistId - ID of the generated playlist
 * @returns {JSX.Element} Renders the playlist modal with playlist details and control options
 */
const ViewPlaylist: React.FC<ViewPlaylistProps> = ({
  isOpen,
  onRequestClose,
  playlistName,
  songs,
  loading,
  handlePlay,
  handleSavePlaylist,
  successMessage,
  username,
  playlistId,
}) => {
  const SpotifyPlaylist = playlistId?.split("/playlist/")[1];
  console.log(SpotifyPlaylist);
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="Playlist Modal"
      overlayClassName="customOverlay"
      className="customModal"
    >
      <h2>{playlistName}</h2>

      {loading ? (
        <p>Loading...</p>
      ) : (
        <div>
          <ul>
            {songs.length > 0 ? (
              songs.map((song, index) => (
                <li key={index}>
                  {song.track.name} by {song.track.artists[0].name}
                  <audio controls>
                    <source src={song.track.preview_url} />
                  </audio>
                </li>
              ))
            ) : (
              <p></p>
            )}
          </ul>
        </div>
      )}

      <PlaylistTracks username={username} playlistId={SpotifyPlaylist} />

      {/* Butoane de control */}
      <div className="button-container">
        <button className="create" onClick={handlePlay}>
          Play Playlist
        </button>
        <button className="create" onClick={handleSavePlaylist}>
          Save Playlist
        </button>
        <button className="create-close" onClick={onRequestClose}>
          Close
        </button>
      </div>

      {successMessage && <p style={{ color: "green" }}>{successMessage}</p>}
    </Modal>
  );
};

export default ViewPlaylist;