package com.ubb.zenith.service;

import com.ubb.zenith.dto.PlaylistDTO;
import com.ubb.zenith.exception.PlaylistAlreadyExistsException;
import com.ubb.zenith.exception.PlaylistNotFoundException;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.Song;
import com.ubb.zenith.repository.PlaylistRepository;
import com.ubb.zenith.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    /**
     * Retrieves all playlists from the repository.
     *
     * @return a list of all available playlists.
     */
    public List<Playlist> getAll() {
        return playlistRepository.findAll();
    }

    /**
     * Adds a new playlist, first checking if a playlist with the same name already exists.
     *
     * @param playlistDTO the DTO object containing the playlist information to be added.
     * @return the added playlist.
     * @throws PlaylistAlreadyExistsException if a playlist with the same name already exists.
     */
    public Playlist add(final PlaylistDTO playlistDTO) throws PlaylistAlreadyExistsException {
        checkIfPlaylistAlreadyExists(playlistDTO.getName());
        return add(buildPlaylist(playlistDTO));
    }

    /**
     * Checks if a playlist with a given name already exists in the repository.
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
     * @param playlistDTO the DTO object containing the information for building the playlist.
     * @return a constructed Playlist object.
     */
    public Playlist buildPlaylist(final PlaylistDTO playlistDTO) {
        var playlist = new Playlist();
        playlist.setName(playlistDTO.getName());
        playlist.setSongs(songRepository.findAll().stream()
                .filter(song -> song.getId().equals(playlistDTO.getId_song())).toList());
        return playlist;
    }

    /**
     * Adds a playlist to the repository.
     *
     * @param playlist the Playlist object to be added.
     * @return the playlist saved in the repository.
     */
    public Playlist add(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    /**
     * Updates an existing playlist based on the old name and the new information from PlaylistDTO.
     *
     * @param oldName the old name of the playlist to be updated.
     * @param playlistDTO the DTO object with the new playlist information.
     * @return the updated playlist.
     * @throws PlaylistNotFoundException if no playlist with the specified name is found.
     */
    public Playlist update(final String oldName, final PlaylistDTO playlistDTO) throws PlaylistNotFoundException {
        return updateName(findPlaylist(oldName), playlistDTO);
    }

    /**
     * Updates the name and songs of an existing playlist.
     *
     * @param playlist the Playlist object to be updated.
     * @param playlistDTO the DTO object with the new playlist information.
     * @return the updated playlist.
     */
    public Playlist updateName(final Playlist playlist, final PlaylistDTO playlistDTO) {
        playlist.setName(playlistDTO.getName());
        playlist.setSongs(songRepository.findAll().stream()
                .filter(song -> song.getId().equals(playlistDTO.getId_song())).toList());
        return playlistRepository.save(playlist);
    }

    /**
     * Finds a playlist by name.
     *
     * @param name the name of the playlist to search for.
     * @return the found playlist.
     * @throws PlaylistNotFoundException if no playlist with the specified name is found.
     */
    public Playlist findPlaylist(final String name) throws PlaylistNotFoundException {
        var modifiedPlaylist = playlistRepository.findAll().stream()
                .filter(playlist -> playlist.getName().equals(name)).findFirst().orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
        return modifiedPlaylist;
    }

    /**
     * Adds a song to an existing playlist.
     *
     * @param name the name of the playlist to add the song to.
     * @param id_song the ID of the song to be added.
     * @throws PlaylistNotFoundException if no playlist with the specified name is found.
     */
    public void addSongToPlaylist(final String name, final Integer id_song) throws PlaylistNotFoundException {
        Playlist playlist = findPlaylist(name);
        Song song = songRepository.findById(id_song).orElseThrow();
        playlist.getSongs().add(song);
        playlistRepository.save(playlist);
    }

    /**
     * Deletes an existing playlist based on its name.
     *
     * @param name the name of the playlist to delete.
     * @throws PlaylistNotFoundException if no playlist with the specified name is found.
     */
    public void delete(final String name) throws PlaylistNotFoundException {
        playlistRepository.delete(findPlaylist(name));
    }
}
