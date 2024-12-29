package com.ubb.zenith.controller;

import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.User;
import com.ubb.zenith.service.SpotifyApiService;
import com.ubb.zenith.service.SpotifyAuthService;
import com.ubb.zenith.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.FormBody;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping("/api/spotify")
public class SpotifyAuthController {

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
    private UserService userService;

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam String code, @RequestParam String state) {
        try {
            if (code == null || state == null) {
                return ResponseEntity.badRequest().body("Missing required parameters.");
            }
            String username = state;
            JSONObject tokenResponse = spotifyAuthService.exchangeCodeForToken(code);
            String accessToken = tokenResponse.getString("access_token");
            String refreshToken = tokenResponse.getString("refresh_token");
            int expiresIn = tokenResponse.getInt("expires_in");

            userService.saveTokens(username, accessToken, refreshToken, expiresIn);
            return ResponseEntity.ok("Tokens saved successfully for " + username);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error exchanging code: " + e.getMessage());
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/view-playlist")
    public ResponseEntity<String> viewPlaylist(
            @RequestParam String username,
            @RequestParam String playlistId) {
        try {
            // Obține token-ul de acces al utilizatorului
            String accessToken = userService.getAccessToken(username);

            // Creează URL-ul pentru Spotify API
            String url = "https://api.spotify.com/v1/playlists/" + playlistId;

            // Creează cererea pentru a obține detalii despre playlist
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // Returnează detalii despre playlist
            return ResponseEntity.ok(response.getBody());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("User not found: " + username);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving playlist details: " + e.getMessage());
        }
    }


    @GetMapping("/generate-playlist-from-quiz")
    public ResponseEntity<String> generatePlaylistFromQuiz(
            @RequestParam String username,
            @RequestParam int question1,
            @RequestParam int question2,
            @RequestParam int question3,
            @RequestParam int question4,
            @RequestParam String playlistName) {
        try {
            String accessToken = userService.getAccessToken(username);

            // Generează query pe baza răspunsurilor la întrebări
            String query = spotifyApiService.generateQueryBasedOnQuizAnswers(question1, question2, question3, question4);

            // Obține track-uri
            List<String> trackUris = spotifyApiService.searchTracks(query, accessToken);

            // Creează playlist pe Spotify
            String userId = spotifyApiService.getCurrentUserId(accessToken);
            String playlistId = spotifyApiService.createPlaylist(userId, playlistName, "Generated from quiz", true, accessToken);

            // Adaugă melodii în playlist
            spotifyApiService.addTracksToPlaylist(playlistId, trackUris, accessToken);

            // Returnează răspuns cu ID-ul playlist-ului și link-ul către Spotify
            return ResponseEntity.ok("Playlist created successfully! Playlist ID: " + playlistId +
                    ". View it on Spotify: https://open.spotify.com/playlist/" + playlistId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("User not found: " + username);
        }
    }


    private String generateQueryBasedOnMood(int happinessScore, int energyScore) {
        if (happinessScore > 7 && energyScore > 7) {
            return "happy upbeat";
        } else if (happinessScore > 7) {
            return "happy calm";
        } else if (energyScore > 7) {
            return "energetic workout";
        } else {
            return "relaxing chill";
        }
    }


    private int calculateEnergyScore(int question3, int question4) {
        // Exemplu simplu: suma răspunsurilor normalizează între 0 și 10
        return Math.min((question3 + question4) / 2, 10);
    }



    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(@RequestParam String username) {
        try {
            User user = userService.findByUsername(username);
            String accessToken = user.getSpotifyAccessToken();

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = "https://api.spotify.com/v1/me";
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response;
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @RequestMapping(value = "/callback/exchange", method = GET)
    public ResponseEntity<String> handleSpotifyCallback(@RequestParam String code) {
        String tokenEndpoint = "https://accounts.spotify.com/api/token";

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("redirect_uri", redirectUri) // Utilizează redirectUri direct
                .build();

        String encodedCredentials = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());

        Request request = new Request.Builder()
                .url(tokenEndpoint)
                .post(formBody)
                .addHeader("Authorization", "Basic " + encodedCredentials)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return ResponseEntity.ok("Token exchange successful: " + responseBody);
            } else {
                String errorBody = response.body().string();
                return ResponseEntity.status(response.code()).body("Failed to retrieve token: " + errorBody);
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }


}

