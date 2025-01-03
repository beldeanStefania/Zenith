package com.ubb.zenith.service;

import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.UserRepository;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
/**
 * Service for managing Spotify authorization and token-related operations.
 */
@Service
public class SpotifyAuthService {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.redirect-uri}") // Asigură-te că ai definit această variabilă în fișierul de configurare
    private String redirectUri;

    @Autowired
    private UserRepository userRepository;

    private final OkHttpClient client = new OkHttpClient();
    /**
     * Retrieves a Spotify access token using the client credentials flow.
     *
     * @return the access token as a string.
     * @throws IOException if the request to Spotify fails.
     */
    public String getAccessToken() throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        String encodedCredentials = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());

        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token")
                .post(formBody)
                .addHeader("Authorization", "Basic " + encodedCredentials)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject jsonResponse = new JSONObject(response.body().string());
                return jsonResponse.getString("access_token");
            } else {
                throw new IOException("Failed to get access token: " + response.body().string());
            }
        }
    }
    /**
     * Generates a Spotify authorization URL for the user.
     *
     * @param username the username for state identification.
     * @return the authorization URL as a string.
     */
    public String getSpotifyAuthorizationUrl(String username) {
        String scopes = "playlist-modify-public playlist-modify-private user-modify-playback-state streaming user-read-playback-state";
        String encodedScopes = URLEncoder.encode(scopes, StandardCharsets.UTF_8);
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);

        System.out.println("Using redirect_uri: " + redirectUri);

        return "https://accounts.spotify.com/authorize"
                + "?client_id=" + clientId
                + "&response_type=code"
                + "&redirect_uri=" + encodedRedirectUri
                + "&scope=" + encodedScopes
                + "&state=" + username;
    }

    /**
     * Refreshes a Spotify access token using a refresh token.
     *
     * @param refreshToken the refresh token provided by Spotify.
     * @return the new access token as a string.
     * @throws IOException if the request to Spotify fails.
     */

    public String refreshAccessToken(String refreshToken) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", refreshToken)
                .build();

        String encodedCredentials = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());

        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token")
                .post(formBody)
                .addHeader("Authorization", "Basic " + encodedCredentials)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject jsonResponse = new JSONObject(response.body().string());
                return jsonResponse.getString("access_token");
            } else {
                throw new IOException("Failed to refresh access token: " + response.body().string());
            }
        }
    }
    /**
     * Exchanges an authorization code for Spotify tokens.
     *
     * @param code the authorization code received from Spotify.
     * @return a JSON object containing access and refresh tokens.
     * @throws IOException if the request to Spotify fails.
     */
    public JSONObject exchangeCodeForToken(String code) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("redirect_uri", redirectUri)
                .build();

        String encodedCredentials = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());

        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token")
                .post(formBody)
                .addHeader("Authorization", "Basic " + encodedCredentials)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                System.out.println("Token Response: " + responseBody);
                return new JSONObject(responseBody);
            } else {
                throw new IOException("Failed to exchange code: " + response.body().string());
            }
        }
    }

    /**
     * Saves Spotify tokens for a user in the database.
     *
     * @param username     the username of the user.
     * @param accessToken  the Spotify access token.
     * @param refreshToken the Spotify refresh token.
     * @param expiresIn    the token expiry time in seconds.
     * @throws UserNotFoundException if the user does not exist.
     */
    public void saveTokens(String username, String accessToken, String refreshToken, int expiresIn) throws UserNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        System.out.println("Saving tokens for user: " + username);
        user.setSpotifyAccessToken(accessToken);
        user.setSpotifyTokenExpiry(LocalDateTime.now().plusSeconds(expiresIn));

        if (refreshToken != null) {
            user.setSpotifyRefreshToken(refreshToken);
        }

        userRepository.save(user);
        System.out.println("Tokens saved to database.");
    }




}
