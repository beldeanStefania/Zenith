package com.ubb.zenith.service;

import com.ubb.zenith.dto.SongDTO;
import com.ubb.zenith.exception.*;
import com.ubb.zenith.model.Mood;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.Song;
import com.ubb.zenith.repository.MoodRepository;
import com.ubb.zenith.repository.PlaylistRepository;
import com.ubb.zenith.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MoodRepository moodRepository;

    // Get all songs
    public List<Song> getAll() {
        return songRepository.findAll();
    }

    // Add a new song (without playlist)
    public Song add(final SongDTO songDTO) throws SongAlreadyExistsException, MoodNotFoundException {
        checkIfSongAlreadyExists(songDTO.getTitle(), songDTO.getArtist());
        return songRepository.save(buildSong(songDTO));
    }

    // Check if song already exists
    private void checkIfSongAlreadyExists(final String title, final String artist) throws SongAlreadyExistsException {
        if (songRepository.findByArtistAndTitle(artist, title).isPresent()) {
            throw new SongAlreadyExistsException("Song already exists");
        }
    }

    // Build a Song entity without assigning a playlist
    private Song buildSong(final SongDTO songDTO) throws MoodNotFoundException {
        // Create the new Song entity
        Song song = new Song();
        song.setArtist(songDTO.getArtist());
        song.setTitle(songDTO.getTitle());
        song.setGenre(songDTO.getGenre());

        Mood mood = moodRepository.findById(songDTO.getMoodId())
                .orElseThrow(() -> new MoodNotFoundException("Mood not found"));

        song.setMood(mood);

        // Initially, no playlist is assigned
        song.setPlaylist(null);

        return song;
    }

    // Update song details without modifying the playlist
    public Song update(final String title, final String artist, final SongDTO newSongDTO) throws SongNotFoundException {
        Song song = songRepository.findByArtistAndTitle(artist, title)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        // Update song details
        song.setArtist(newSongDTO.getArtist());
        song.setTitle(newSongDTO.getTitle());
        song.setGenre(newSongDTO.getGenre());

        return songRepository.save(song);
    }

    // Assign a playlist to a song (separate method)
    public Song assignPlaylistToSong(Integer songId, Integer playlistId) throws SongNotFoundException, PlaylistNotFoundException {
        // Find the song
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        // Find the playlist
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));

        // Assign the playlist to the song
        song.setPlaylist(playlist);

        // Save the updated song
        return songRepository.save(song);
    }

    // Delete a song by its ID
    public void delete(Integer songId) throws SongNotFoundException {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));
        songRepository.delete(song);
    }

    // Find a song by artist and title
    public Song findSong(final String artist, final String title) throws SongNotFoundException {
        return songRepository.findByArtistAndTitle(artist, title)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));
    }

    // Find the playlist by ID
    private Playlist findPlaylistById(Integer playlistId) throws PlaylistNotFoundException {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found with id " + playlistId));
    }
}
