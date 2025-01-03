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
    /**
     * Endpoint to generate Spotify login URL.
     * @param username The username of the user initiating login.
     * @return URL to redirect the user for Spotify login.
     */
    @GetMapping("/login")
    public ResponseEntity<String> loginToSpotify(@RequestParam String username) {
        String url = spotifyAuthService.getSpotifyAuthorizationUrl(username);
        return ResponseEntity.ok(url);
    }

    /**
     * Endpoint to handle authentication after login.
     * @param username The username of the authenticated user.
     * @param code The authorization code received from Spotify.
     * @return Success or error message.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateWithSpotify(
            @RequestParam String username,
            @RequestParam String code) {
        try {
            System.out.println("Authenticating user: " + username);
            // Retrieve user from database or throw an exception if not found.

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
            // Exchange authorization code for access and refresh tokens.

            JSONObject tokens = spotifyAuthService.exchangeCodeForToken(code);

            System.out.println("Received tokens: " + tokens.toString());
            // Extract tokens and expiration details.

            String accessToken = tokens.getString("access_token");
            String refreshToken = tokens.optString("refresh_token", null);
            int expiresIn = tokens.getInt("expires_in");
            // Save tokens in the database for the user.

            spotifyAuthService.saveTokens(username, accessToken, refreshToken, expiresIn);

            System.out.println("Tokens saved successfully for user: " + username);

            return ResponseEntity.ok("User authenticated and tokens saved successfully!");
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace for debugging
            return ResponseEntity.status(500).body("Error during authentication: " + e.getMessage());
        }
    }

    /**
     * Spotify callback handler to process the authorization code.
     * @param code The authorization code from Spotify.
     * @param state The username used as state during login.
     * @return Success or error message.
     */    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam String code, @RequestParam String state) {
        try {
            if (code == null || state == null) {
                return ResponseEntity.badRequest().body("Missing required parameters.");
            }
            // Extract the username from the state parameter.

            String username = state;
            // Exchange the code for access and refresh tokens.

            var tokenResponse = spotifyAuthService.exchangeCodeForToken(code);
            String accessToken = tokenResponse.getString("access_token");
            String refreshToken = tokenResponse.getString("refresh_token");
            int expiresIn = tokenResponse.getInt("expires_in");
            // Save the tokens for the user.

            userService.saveTokens(username, accessToken, refreshToken, expiresIn);
            return ResponseEntity.ok("Tokens saved successfully for " + username);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error exchanging code: " + e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("User not found: " + e.getMessage());
        }
    }


    /**
     * Endpoint to generate a Spotify playlist based on mood scores.
     * @param username The username of the user.
     * @param happinessScore Score indicating the user's happiness.
     * @param energyScore Score indicating the user's energy level.
     * @param sadnessScore Score indicating the user's sadness.
     * @param loveScore Score indicating the user's affection or love.
     * @param playlistName The name for the new playlist.
     * @return URL of the created playlist or an error message.
     */    @PostMapping("/generate-playlist")
    public ResponseEntity<String> generatePlaylistFromMood(
            @RequestParam String username,
            @RequestParam int happinessScore,
            @RequestParam int energyScore,
            @RequestParam int sadnessScore,
            @RequestParam int loveScore,
            @RequestParam String playlistName) {
        try {
            // Get the access token for the user.
            String accessToken = userService.getAccessToken(username);

            // Normalize mood scores to a range of 0.0 to 1.0.
            double normalizedHappiness = (happinessScore - 1) / 4.0;
            double normalizedEnergy = (energyScore - 1) / 4.0;
            double normalizedSadness = (sadnessScore - 1) / 4.0;
            double normalizedLove = (loveScore - 1) / 4.0;

            // Generate a Spotify query based on mood scores.
            String query = spotifyApiService.generateQueryBasedOnMood(normalizedHappiness, normalizedEnergy, normalizedSadness, normalizedLove);

            // Search for tracks that match the mood query.
            List<String> trackUris = spotifyApiService.searchTracks(query, normalizedHappiness, normalizedEnergy, accessToken);

            // Create a new playlist and add the tracks.
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




    /**
     * Endpoint to view details of a Spotify playlist.
     * @param username The username of the user.
     * @param playlistId The ID of the playlist.
     * @return Playlist details in JSON format or an error message.
     */    @GetMapping("/view-playlist")
    public ResponseEntity<String> viewPlaylist(@RequestParam String username, @RequestParam String playlistId) {
        try {            // Get the access token for the user.

            String accessToken = userService.getAccessToken(username);
            // Fetch playlist details from Spotify API.

            String playlistDetails = spotifyApiService.getPlaylistDetails(playlistId, accessToken);
            return ResponseEntity.ok(playlistDetails);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("User not found: " + username);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving playlist details: " + e.getMessage());
        }
    }
    /**
     * Endpoint to play a Spotify playlist.
     * @param username The username of the user.
     * @param playlistId The ID or URL of the playlist to play.
     * @return Success or error message.
     */
    @PostMapping("/play-playlist")
    public ResponseEntity<String> playPlaylist(
            @RequestParam String username,
            @RequestParam String playlistId) {
        try {
            // Get the access token for the user.
            String accessToken = userService.getAccessToken(username);

            // Extract the playlist ID from the URL, if necessary.
            if (playlistId.contains("https://open.spotify.com/playlist/")) {
                playlistId = playlistId.substring(playlistId.lastIndexOf("/") + 1);
            }

            // Get the list of track URIs in the playlist.
            List<String> trackUris = spotifyApiService.getTracksFromPlaylist(playlistId, accessToken);

            // Send a request to Spotify API to play the tracks.
            spotifyApiService.playTracks(trackUris, accessToken);

            return ResponseEntity.ok("Playlist playback started successfully!");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("User not found: " + username);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


}
