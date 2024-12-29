package com.ubb.zenith.service;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class SpotifyAuthService {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.redirect-uri}") // Asigură-te că ai definit această variabilă în fișierul de configurare
    private String redirectUri;

    private final OkHttpClient client = new OkHttpClient();

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

    public String getSpotifyAuthorizationUrl(String username) {
        String scopes = "playlist-modify-public playlist-modify-private";
        String redirectUri = URLEncoder.encode("{REDIRECT_URI}", StandardCharsets.UTF_8);
        return "https://accounts.spotify.com/authorize?client_id=" + clientId +
                "&response_type=code&redirect_uri=" + redirectUri +
                "&scope=" + scopes +
                "&state=" + username; // Include username în state
    }


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
                return new JSONObject(response.body().string());
            } else {
                throw new IOException("Failed to exchange code: " + response.body().string());
            }
        }
    }
}
