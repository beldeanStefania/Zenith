import Modal from "react-modal";
import PlaylistTracks from "./PlaylistTracks";

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
