package com.ubb.zenith.service;

import com.ubb.zenith.dto.PlaylistDTO;
import com.ubb.zenith.exception.PlaylistAlreadyExistsException;
import com.ubb.zenith.exception.PlaylistNotFoundException;
import com.ubb.zenith.exception.SongAlreadyExistsException;
import com.ubb.zenith.exception.SongNotFoundException;
import com.ubb.zenith.model.Mood;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.Song;
import com.ubb.zenith.repository.MoodRepository;
import com.ubb.zenith.repository.PlaylistRepository;
import com.ubb.zenith.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private MoodRepository moodRepository;

    /**
     * Retrieves all playlists from the repository.
     *
     * @return a list of all available playlists.
     */
    public List<Playlist> getAll() {
        return playlistRepository.findAll();
    }

    /**
     * Adds a new playlist after verifying if a playlist with the same name already exists.
     *
     * @param playlistDTO DTO object that contains the information of the playlist to be added.
     * @return the added playlist.
     * @throws PlaylistAlreadyExistsException if a playlist with the same name already exists.
     */
    public Playlist add(final PlaylistDTO playlistDTO) throws PlaylistAlreadyExistsException {
        checkIfPlaylistAlreadyExists(playlistDTO.getName());
        return playlistRepository.save(buildPlaylist(playlistDTO));
    }

    /**
     * Checks if a playlist with a specific name exists in the repository.
     *
     * @param name the name of the playlist to be checked.
     * @throws PlaylistAlreadyExistsException if the playlist already exists.
     */
    public void checkIfPlaylistAlreadyExists(final String name) throws PlaylistAlreadyExistsException {
        if (playlistRepository.findByName(name).isPresent()) {
            throw new PlaylistAlreadyExistsException("Playlist already exists");
        }
    }

    /**
     * Builds a Playlist object from a PlaylistDTO.
     *
     * @param playlistDTO DTO object that contains the information to build the playlist.
     * @return a built Playlist object.
     */
    public Playlist buildPlaylist(final PlaylistDTO playlistDTO) {
        var playlist = new Playlist();
        playlist.setName(playlistDTO.getName());
        playlist.setSongs(List.of());  // Initially, the playlist will have no songs
        return playlist;
    }

    /**
     * Updates an existing playlist based on the old name and the new information from PlaylistDTO.
     *
     * @param oldName the old name of the playlist to be updated.
     * @param playlistDTO DTO object with the new information of the playlist.
     * @return the updated playlist.
     * @throws PlaylistNotFoundException if no playlist with the specified name is found.
     */
    public Playlist update(final String oldName, final PlaylistDTO playlistDTO) throws PlaylistNotFoundException {
        Playlist playlist = findPlaylist(oldName);
        playlist.setName(playlistDTO.getName());  // Only updating the name
        return playlistRepository.save(playlist);
    }

    /**
     * Finds a playlist by name.
     *
     * @param name the name of the playlist to be searched.
     * @return the found playlist.
     * @throws PlaylistNotFoundException if no playlist with the specified name is found.
     */
    public Playlist findPlaylist(final String name) throws PlaylistNotFoundException {
        return playlistRepository.findByName(name)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
    }

    /**
     * Adds a song to an existing playlist.
     *
     * @param playlistName the name of the playlist.
     * @param songId the ID of the song to be added.
     * @throws PlaylistNotFoundException if the playlist is not found.
     * @throws SongNotFoundException if the song is not found.
     */
    public Playlist addSongToPlaylist(final String playlistName, final Integer songId) throws PlaylistNotFoundException, SongNotFoundException, SongAlreadyExistsException {
        Playlist playlist = findPlaylist(playlistName);  // Find the playlist by name
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        checkSongInPlaylist(song);  // Check if the song is already in the playlist

        // Add song to playlist and set playlist in song (managing both sides of the relationship)
        playlist.getSongs().add(song);
        song.setPlaylist(playlist);  // Set playlist to the song

        // Save the song and playlist (Cascade will save both sides if set up)
        songRepository.save(song);
        return playlistRepository.save(playlist);
    }

    public void checkSongInPlaylist(Song song) throws SongAlreadyExistsException {
        if(playlistRepository.findBySongs(song).isPresent()) {
            throw new SongAlreadyExistsException("Song already exists");
        }
    }

    /**
     * Deletes an existing playlist based on the name.
     *
     * @param name the name of the playlist to be deleted.
     * @throws PlaylistNotFoundException if no playlist with the specified name is found.
     */
    public void delete(final String name) throws PlaylistNotFoundException {
        Playlist playlist = findPlaylist(name);
        playlistRepository.delete(playlist);
    }


    // Generate a playlist based on user's mood scores
    public Playlist generatePlaylistForUser(Integer happinessScore, Integer sadnessScore, Integer loveScore, Integer energyScore, String playlistName) {
        List<Mood> allMoods = moodRepository.findAll();

        // Filter and collect songs based on matching moods
        List<Song> matchingSongs = allMoods.stream()
                .filter(mood -> isMoodMatch(mood, happinessScore, sadnessScore, loveScore, energyScore))
                .flatMap(mood -> mood.getSongs().stream())
                .collect(Collectors.toList());

        // Create a new playlist
        Playlist playlist = new Playlist();
        playlist.setName(playlistName);

        // Set the playlist in each song
        matchingSongs.forEach(song -> song.setPlaylist(playlist));  // Ensure each song knows it's part of this playlist

        // Set songs in the playlist
        playlist.setSongs(matchingSongs);

        // Save the playlist and songs (cascade should handle saving both)
        return playlistRepository.save(playlist);
    }

    // method to determine if a song's mood matches the user's mood scores
    private boolean isMoodMatch(Mood mood, Integer happinessScore, Integer sadnessScore, Integer loveScore, Integer energyScore) {
        // Define a threshold for matching (e.g., +/- 2 points)
        int threshold = 2;

        return Math.abs(mood.getHappiness_score() - happinessScore) <= threshold
                && Math.abs(mood.getSadness_score() - sadnessScore) <= threshold
                && Math.abs(mood.getLove_score() - loveScore) <= threshold
                && Math.abs(mood.getEnergy_score() - energyScore) <= threshold;
    }
}
