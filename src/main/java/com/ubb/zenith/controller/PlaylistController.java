package com.ubb.zenith.controller;

import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.PlaylistRepository;
import com.ubb.zenith.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.service.PlaylistService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {


    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping("/add")
    public ResponseEntity<Playlist> addPlaylist(@RequestParam String username, @RequestParam String name, @RequestParam String mood,  @RequestParam String spotifyPlaylistId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        Playlist playlist = new Playlist();
        playlist.setUser(user);
        playlist.setName(name);
        playlist.setMood(mood);
        playlist.setCreatedAt(LocalDate.now());
        playlist.setSpotifyPlaylistId(spotifyPlaylistId);

        return ResponseEntity.ok(playlistRepository.save(playlist));
    }


    @GetMapping("/getPlaylists/{username}")
    public ResponseEntity<List<Playlist>> getUserPlaylists(@PathVariable String username) {
        List<Playlist> playlists = playlistService.findPlaylistsByUsername(username);
        if (playlists.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(playlists);
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Playlist>> getUserPlaylists(@PathVariable Integer userId) {
        List<Playlist> playlists = playlistService.getUserPlaylists(userId);
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylist(@PathVariable Integer id) {
        Playlist playlist = playlistService.getPlaylist(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id: " + id));
        return ResponseEntity.ok(playlist);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable Integer id, @RequestParam String name, @RequestParam String mood, @RequestParam String spotifyPlaylistId) {
        Playlist updatedPlaylist = playlistService.updatePlaylist(id, name, mood, spotifyPlaylistId);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlaylist(@PathVariable Integer id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.ok("Playlist deleted successfully.");
    }
}
