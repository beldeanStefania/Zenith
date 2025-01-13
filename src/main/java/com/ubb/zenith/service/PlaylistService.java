package com.ubb.zenith.service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.PlaylistRepository;
import com.ubb.zenith.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserRepository userRepository;

    public Playlist savePlaylist(Integer userId, String name, String mood,  String spotifyPlaylistId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Playlist playlist = new Playlist();
        playlist.setUser(user);
        playlist.setName(name);
        playlist.setMood(mood);
        playlist.setCreatedAt(LocalDate.now());
        playlist.setSpotifyPlaylistId(spotifyPlaylistId);

        return playlistRepository.save(playlist);
    }

    public List<Playlist> findPlaylistsByUsername(String username) {
        return playlistRepository.findByUserUsername(username);
    }


    public List<Playlist> getUserPlaylists(Integer userId) {
        return playlistRepository.findByUserId(userId);
    }

    public Optional<Playlist> getPlaylist(Integer id) {
        return playlistRepository.findById(id);
    }

    @Transactional
    public Playlist updatePlaylist(Integer id, String newName, String newMood,  String newSpotifyPlaylistId) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id: " + id));
        playlist.setName(newName);
        playlist.setMood(newMood);
        playlist.setSpotifyPlaylistId(newSpotifyPlaylistId);
        return playlist;
    }

    @Transactional
    public void deletePlaylist(Integer id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id: " + id));
        playlistRepository.delete(playlist);
    }
}
