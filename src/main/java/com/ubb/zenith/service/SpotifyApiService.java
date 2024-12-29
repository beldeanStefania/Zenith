package com.ubb.zenith.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpotifyApiService {

    private final OkHttpClient client = new OkHttpClient();

    // Caută melodii bazate pe un query
    public List<String> searchTracks(String query, String accessToken) throws IOException {
        String url = "https://api.spotify.com/v1/search?q=" + query + "&type=track&limit=10";

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

    // Generează un query bazat pe răspunsurile la întrebări
    public String generateQueryBasedOnQuizAnswers(int question1, int question2, int question3, int question4) {
        int happinessScore = (question1 + question2) / 2;
        int energyScore = (question3 + question4) / 2;

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
}
