package com.ubb.zenith.controller;

import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.UserRepository;
import com.ubb.zenith.service.SpotifyApiService;
import com.ubb.zenith.service.SpotifyAuthService;
import com.ubb.zenith.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    @Autowired
    private SpotifyAuthService spotifyAuthService;

    @Autowired
    private SpotifyApiService spotifyApiService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ResponseEntity<String> loginToSpotify(@RequestParam String username) {
        String url = spotifyAuthService.getSpotifyAuthorizationUrl(username);
        return ResponseEntity.ok(url);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateWithSpotify(
            @RequestParam String username,
            @RequestParam String code) {
        try {
            // Verifică dacă utilizatorul există în baza de date
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

            // Schimbă codul pentru token-uri utilizând Spotify API
            JSONObject tokens = spotifyAuthService.exchangeCodeForToken(code);

            // Extrage token-urile din răspunsul Spotify
            String accessToken = tokens.getString("access_token");
            String refreshToken = tokens.optString("refresh_token", null); // Refresh token-ul poate lipsi
            int expiresIn = tokens.getInt("expires_in");

            // Salvează token-urile în baza de date
            spotifyAuthService.saveTokens(user, accessToken, refreshToken, expiresIn);

            return ResponseEntity.ok("User authenticated and tokens saved successfully!");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("User not found: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error during authentication: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }


    // Endpoint pentru autentificare cu Spotify și stocarea token-urilor
    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam String code, @RequestParam String state) {
        try {
            if (code == null || state == null) {
                return ResponseEntity.badRequest().body("Missing required parameters.");
            }
            String username = state;
            var tokenResponse = spotifyAuthService.exchangeCodeForToken(code);
            String accessToken = tokenResponse.getString("access_token");
            String refreshToken = tokenResponse.getString("refresh_token");
            int expiresIn = tokenResponse.getInt("expires_in");

            userService.saveTokens(username, accessToken, refreshToken, expiresIn);
            return ResponseEntity.ok("Tokens saved successfully for " + username);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error exchanging code: " + e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("User not found: " + e.getMessage());
        }
    }

    // Endpoint pentru generarea unui playlist bazat pe mood
    @PostMapping("/generate-playlist")
    public ResponseEntity<String> generatePlaylistFromMood(
            @RequestParam String username,
            @RequestParam int happinessScore,
            @RequestParam int energyScore,
            @RequestParam int sadnessScore,
            @RequestParam int loveScore,
            @RequestParam String playlistName) {
        try {
            String accessToken = userService.getAccessToken(username);

            // Normalizarea scorurilor de la 1-5 la 0-1
            double normalizedHappiness = (happinessScore - 1) / 4.0;
            double normalizedEnergy = (energyScore - 1) / 4.0;
            double normalizedSadness = (sadnessScore - 1) / 4.0;
            double normalizedLove = (loveScore - 1) / 4.0;

            // Generează query bazat pe mood
            String query = spotifyApiService.generateQueryBasedOnMood(normalizedHappiness, normalizedEnergy, normalizedSadness, normalizedLove);

            // Caută track-uri relevante folosind filtrele normalizate
            List<String> trackUris = spotifyApiService.searchTracks(query, normalizedHappiness, normalizedEnergy, accessToken);

            // Creează playlist și adaugă track-uri
            String userId = spotifyApiService.getCurrentUserId(accessToken);
            String playlistId = spotifyApiService.createPlaylist(userId, playlistName, "Generated based on mood", true, accessToken);
            spotifyApiService.addTracksToPlaylist(playlistId, trackUris, accessToken);

            return ResponseEntity.ok("https://open.spotify.com/playlist/" + playlistId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("User not found: " + username);
        }
    }



    // Endpoint pentru vizualizarea detaliilor unui playlist
    @GetMapping("/view-playlist")
    public ResponseEntity<String> viewPlaylist(@RequestParam String username, @RequestParam String playlistId) {
        try {
            String accessToken = userService.getAccessToken(username);
            String playlistDetails = spotifyApiService.getPlaylistDetails(playlistId, accessToken);
            return ResponseEntity.ok(playlistDetails);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("User not found: " + username);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving playlist details: " + e.getMessage());
        }
    }

    @PostMapping("/play-playlist")
    public ResponseEntity<String> playPlaylist(
            @RequestParam String username,
            @RequestParam String playlistId) {
        try {
            // Obține token-ul de acces al utilizatorului
            String accessToken = userService.getAccessToken(username);

            // Extrage doar ID-ul playlistului din URL (dacă este cazul)
            if (playlistId.contains("https://open.spotify.com/playlist/")) {
                playlistId = playlistId.substring(playlistId.lastIndexOf("/") + 1);
            }

            // Obține lista de track-uri din playlist
            List<String> trackUris = spotifyApiService.getTracksFromPlaylist(playlistId, accessToken);

            // Trimite cererea pentru redare către API-ul Spotify
            spotifyApiService.playTracks(trackUris, accessToken);

            return ResponseEntity.ok("Playlist playback started successfully!");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("User not found: " + username);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


}
