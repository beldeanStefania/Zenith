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

    /**
     * Searches for tracks on Spotify based on the query, valence, and energy parameters.
     *
     * @param query      The search query (e.g., keywords or artist name).
     * @param valence    The target valence (musical positiveness, scale: 0.0 to 1.0).
     * @param energy     The target energy level (scale: 0.0 to 1.0).
     * @param accessToken The Spotify API access token.
     * @return A list of track URIs that match the search criteria.
     * @throws IOException If the API request fails.
     */
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

    /**
     * Fetches details of a specific playlist by its ID.
     *
     * @param playlistId The Spotify playlist ID.
     * @param accessToken The Spotify API access token.
     * @return The playlist details as a JSON string.
     * @throws IOException If the API request fails.
     */
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

            return response.body().string();
        }
    }
    /**
     * Adds tracks to an existing Spotify playlist.
     *
     * @param playlistId The Spotify playlist ID.
     * @param trackUris  A list of track URIs to add to the playlist.
     * @param accessToken The Spotify API access token.
     * @throws IOException If the API request fails.
     */

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
    /**
     * Creates a new playlist for a specified user.
     *
     * @param userId      The Spotify user ID.
     * @param playlistName The name of the new playlist.
     * @param description  The playlist description.
     * @param isPublic     Whether the playlist should be public.
     * @param accessToken  The Spotify API access token.
     * @return The ID of the created playlist.
     * @throws IOException If the API request fails.
     */
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

    /**
     * Retrieves the Spotify ID of the currently authenticated user.
     *
     * @param accessToken The Spotify API access token.
     * @return The user ID.
     * @throws IOException If the API request fails.
     */
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
    /**
     * Fetches all track URIs from a specific Spotify playlist.
     *
     * @param playlistId The Spotify playlist ID.
     * @param accessToken The Spotify API access token.
     * @return A list of track URIs in the playlist.
     * @throws IOException If the API request fails.
     */
    public List<String> getTracksFromPlaylist(String playlistId, String accessToken) throws IOException {
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch playlist tracks: " + response.body().string());
            }

            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray items = jsonResponse.getJSONArray("items");

            List<String> trackUris = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                String trackUri = items.getJSONObject(i).getJSONObject("track").getString("uri");
                trackUris.add(trackUri);
            }
            return trackUris;
        }
    }

    /**
     * Plays a list of Spotify tracks on the user's active device.
     *
     * @param trackUris   A list of track URIs to play.
     * @param accessToken The Spotify API access token.
     * @throws IOException If the API request fails.
     */
    public void playTracks(List<String> trackUris, String accessToken) throws IOException {
        String url = "https://api.spotify.com/v1/me/player/play";

        JSONObject body = new JSONObject();
        body.put("uris", trackUris);

        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(body.toString(), okhttp3.MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                JSONObject errorResponse = new JSONObject(responseBody);
                String reason = errorResponse.optJSONObject("error").optString("reason", "");

                if ("NO_ACTIVE_DEVICE".equalsIgnoreCase(reason)) {
                    throw new IllegalStateException("No active Spotify device found. Start playback on any device and try again.");
                } else {
                    throw new IOException("Failed to play tracks: " + responseBody);
                }
            }
        }
    }
    /**
     * Generates a search query string based on mood parameters.
     *
     * @param happiness The level of happiness (scale: 0.0 to 1.0).
     * @param energy    The level of energy (scale: 0.0 to 1.0).
     * @param sadness   The level of sadness (scale: 0.0 to 1.0).
     * @param love      The level of love (scale: 0.0 to 1.0).
     * @return A string representing the generated query.
     */

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
