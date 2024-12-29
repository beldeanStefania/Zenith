package com.ubb.zenith.controller;

import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.service.SpotifyApiService;
import com.ubb.zenith.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyApiController {

    @Autowired
    private SpotifyApiService spotifyApiService;

    @Autowired
    private UserService userService;

    @PostMapping("/create-playlist")
    public ResponseEntity<String> createPlaylist(
            @RequestParam String username,
            @RequestParam String playlistName,
            @RequestParam String description,
            @RequestParam boolean isPublic) {
        try {
            // Obține token-ul de acces pentru utilizator
            String accessToken = userService.getAccessToken(username);

            // Obține ID-ul utilizatorului Spotify
            String userId = spotifyApiService.getCurrentUserId(accessToken);

            // Creează playlist-ul folosind Spotify API
            String playlistId = spotifyApiService.createPlaylist(userId, playlistName, description, isPublic, accessToken);

            return ResponseEntity.ok("Playlist created successfully for user: " + username + ", Playlist ID: " + playlistId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("User not found: " + username);
        }
    }

    @GetMapping("/generate-playlist")
    public ResponseEntity<String> generatePlaylistBasedOnMood(
            @RequestParam String username,
            @RequestParam int happinessScore,
            @RequestParam int energyScore,
            @RequestParam String playlistName) {
        try {
            // Obține token-ul de acces
            String accessToken = userService.getAccessToken(username);

            // Creează query-ul bazat pe mood
            String query = generateQueryBasedOnMood(happinessScore, energyScore);

            // Caută melodii
            List<String> trackUris = spotifyApiService.searchTracks(query, accessToken);

            // Obține ID-ul utilizatorului și creează playlist-ul
            String userId = spotifyApiService.getCurrentUserId(accessToken);
            String playlistId = spotifyApiService.createPlaylist(userId, playlistName, "Generated playlist based on mood", false, accessToken);

            // Adaugă melodii în playlist
            spotifyApiService.addTracksToPlaylist(playlistId, trackUris, accessToken);

            return ResponseEntity.ok("Playlist generated and tracks added successfully for user: " + username);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("User not found: " + username);
        }
    }

    // Metodă auxiliară pentru a genera query-ul în funcție de mood
    private String generateQueryBasedOnMood(int happinessScore, int energyScore) {
        if (happinessScore > 7 && energyScore > 7) {
            return "happy upbeat";
        } else if (happinessScore > 7) {
            return "happy calm";
        } else if (energyScore > 7) {
            return "energetic";
        } else {
            return "relaxing";
        }
    }
}
