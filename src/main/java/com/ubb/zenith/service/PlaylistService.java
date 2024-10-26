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
        playlist.setSongs(List.of());
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
        playlist.setName(playlistDTO.getName());
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
        Playlist playlist = findPlaylist(playlistName);
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        checkSongInPlaylist(song);


        playlist.getSongs().add(song);
        song.setPlaylist(playlist);


        songRepository.save(song);
        return playlistRepository.save(playlist);
    }

    /**
     * Checks if a song is already present in any playlist.
     *
     * @param song the song to check for presence in a playlist.
     * @throws SongAlreadyExistsException if the song is already associated with a playlist.
     */
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


    /**
     * Generates a playlist based on the user's mood scores.
     *
     * @param happinessScore the happiness score of the user.
     * @param sadnessScore the sadness score of the user.
     * @param loveScore the love score of the user.
     * @param energyScore the energy score of the user.
     * @param playlistName the name of the playlist to be generated.
     * @return the generated playlist.
     * @throws PlaylistAlreadyExistsException if a playlist with the same name already exists.
     */
    public Playlist generatePlaylistForUser(Integer happinessScore, Integer sadnessScore, Integer loveScore, Integer energyScore, String playlistName) throws PlaylistAlreadyExistsException {
        checkIfPlaylistAlreadyExists(playlistName);
        List<Mood> allMoods = moodRepository.findAll();
        List<Song> matchingSongs = allMoods.stream()
                .filter(mood -> isMoodMatch(mood, happinessScore, sadnessScore, loveScore, energyScore))
                .flatMap(mood -> mood.getSongs().stream())
                .collect(Collectors.toList());
        Playlist playlist = new Playlist();
        playlist.setName(playlistName);


        matchingSongs.forEach(song -> song.setPlaylist(playlist));

        playlist.setSongs(matchingSongs);

        return playlistRepository.save(playlist);
    }

    /**
     * Checks if a mood matches the user's mood scores.
     *
     * @param mood the mood to be checked.
     * @param happinessScore the happiness score of the user.
     * @param sadnessScore the sadness score of the user.
     * @param loveScore the love score of the user.
     * @param energyScore the energy score of the user.
     * @return true if the mood matches the user's mood scores, false otherwise.
     */
    private boolean isMoodMatch(Mood mood, Integer happinessScore, Integer sadnessScore, Integer loveScore, Integer energyScore) {
        // Define a threshold for matching (e.g., +/- 2 points)
        int threshold = 2;

        return Math.abs(mood.getHappiness_score() - happinessScore) <= threshold
                && Math.abs(mood.getSadness_score() - sadnessScore) <= threshold
                && Math.abs(mood.getLove_score() - loveScore) <= threshold
                && Math.abs(mood.getEnergy_score() - energyScore) <= threshold;
    }

    /**
     * Retrieves all songs from a playlist.
     *
     * @param playlistName the name of the playlist.
     * @return a list of all songs in the playlist.
     * @throws PlaylistNotFoundException if no playlist with the specified name is found.
     */
    public List<Song> getSongsFromPlaylist(String playlistName) throws PlaylistNotFoundException {
        Playlist playlist = playlistRepository.findByName(playlistName)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
        return playlist.getSongs();
    }
}
