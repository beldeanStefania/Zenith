package com.ubb.zenith.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpotifyApiService {

    private final OkHttpClient client = new OkHttpClient();

    // Caută melodii bazate pe un query
    public List<String> searchTracks(String query, double valence, double energy, String accessToken) throws IOException {
        String url = "https://api.spotify.com/v1/search?q=" + query +
                "&type=track&limit=10&valence=" + valence +
                "&energy=" + energy;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to search tracks: " + response.body().string());
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            List<String> trackUris = new ArrayList<>();
            jsonResponse.getJSONObject("tracks").getJSONArray("items").forEach(item -> {
                JSONObject track = (JSONObject) item;
                trackUris.add(track.getString("uri"));
            });

            return trackUris;
        }
    }


    // Adaugă melodii într-un playlist existent
    public void addTracksToPlaylist(String playlistId, List<String> trackUris, String accessToken) throws IOException {
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";

        JSONObject requestBody = new JSONObject();
        requestBody.put("uris", trackUris);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(requestBody.toString(), okhttp3.MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to add tracks to playlist: " + response.body().string());
            }
        }
    }

    // Creează un nou playlist și returnează ID-ul acestuia
    public String createPlaylist(String userId, String playlistName, String description, boolean isPublic, String accessToken) throws IOException {
        String url = "https://api.spotify.com/v1/users/" + userId + "/playlists";

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", playlistName);
        requestBody.put("description", description);
        requestBody.put("public", isPublic);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(requestBody.toString(), okhttp3.MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to create playlist: " + response.body().string());
            }

            JSONObject responseBody = new JSONObject(response.body().string());
            return responseBody.getString("id");
        }
    }

    // Obține ID-ul utilizatorului curent
    public String getCurrentUserId(String accessToken) throws IOException {
        String url = "https://api.spotify.com/v1/me";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to get user profile: " + response.body().string());
            }

            JSONObject jsonResponse = new JSONObject(response.body().string());
            return jsonResponse.getString("id");
        }
    }

    public List<String> getTracksFromPlaylist(String playlistId, String accessToken) throws IOException {
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONArray items = jsonResponse.getJSONArray("items");

        List<String> trackUris = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            String trackUri = items.getJSONObject(i).getJSONObject("track").getString("uri");
            trackUris.add(trackUri);
        }
        return trackUris;
    }

    public void playTracks(List<String> trackUris, String accessToken) throws IOException {
        String url = "https://api.spotify.com/v1/me/player/play";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        JSONObject body = new JSONObject();
        body.put("uris", trackUris);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }

    public String getPlaylistDetails(String playlistId, String accessToken) throws IOException {
        String url = "https://api.spotify.com/v1/playlists/" + playlistId;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch playlist details: " + response.body().string());
            }

            // Returnează detaliile playlist-ului ca String JSON
            return response.body().string();
        }
    }

    public String generateQueryBasedOnMood(double happiness, double energy, double sadness, double love) {
        StringBuilder query = new StringBuilder();

        if (happiness > 0.7) {
            query.append("happy ");
        }
        if (energy > 0.7) {
            query.append("energetic ");
        }
        if (sadness > 0.7) {
            query.append("sad ");
        }
        if (love > 0.7) {
            query.append("romantic ");
        }
        if (query.length() == 0) {
            query.append("relaxing chill "); // Default dacă niciun criteriu nu este dominant
        }

        return query.toString().trim();
    }
}
