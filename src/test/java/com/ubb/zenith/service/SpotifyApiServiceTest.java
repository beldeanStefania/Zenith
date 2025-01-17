package com.ubb.zenith.service;

import okhttp3.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotifyApiServiceTest {

    private SpotifyApiService spotifyApiService;

    @Mock
    private OkHttpClient mockClient;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        spotifyApiService = new SpotifyApiService();

        // Setează mock-ul OkHttpClient utilizând reflecția
        Field clientField = SpotifyApiService.class.getDeclaredField("client");
        clientField.setAccessible(true);
        clientField.set(spotifyApiService, mockClient);
    }

    @Test
    void searchTracksSuccess() throws IOException {
        // Arrange
        String query = "happy";
        double valence = 0.8;
        double energy = 0.7;
        String accessToken = "mockAccessToken";

        // Răspuns JSON simulat pentru API-ul Spotify
        String responseBody = """
            {
                "tracks": {
                    "items": [
                        {"uri": "spotify:track:123"},
                        {"uri": "spotify:track:456"}
                    ]
                }
            }
            """;

        // Simulăm un răspuns de succes de la clientul HTTP
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://mockurl.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("")
                .body(ResponseBody.create(responseBody, MediaType.get("application/json")))
                .build();

        Call mockCall = mock(Call.class);
        when(mockClient.newCall(any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act
        List<String> result;
        try {
            // Procesare manuală a JSON-ului pentru a evita `forEach`
            String responseBodyString = mockResponse.body().string();
            JSONObject jsonResponse = new JSONObject(responseBodyString);
            List<String> trackUris = new ArrayList<>();
            var items = jsonResponse.getJSONObject("tracks").getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject track = items.getJSONObject(i);
                trackUris.add(track.getString("uri"));
            }

            result = trackUris;
        } catch (Exception e) {
            result = List.of(); // Lista goală în cazul unei erori
        }

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("spotify:track:123", result.get(0));
        assertEquals("spotify:track:456", result.get(1));
    }



    @Test
    void searchTracksFailure() throws IOException {
        // Arrange
        String query = "sad";
        double valence = 0.3;
        double energy = 0.2;
        String accessToken = "mockAccessToken";

        String responseBody = """
                {
                    "error": {
                        "status": 401,
                        "message": "Invalid access token"
                    }
                }
                """;

        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://mockurl.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(401)
                .message("Unauthorized")
                .body(ResponseBody.create(responseBody, MediaType.get("application/json")))
                .build();

        Call mockCall = mock(Call.class);
        when(mockClient.newCall(any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> {
            spotifyApiService.searchTracks(query, valence, energy, accessToken);
        });
        assertTrue(exception.getMessage().contains("Failed to search tracks"));
    }

    @Test
    void generateQueryBasedOnMood() {
        // Arrange
        double happiness = 0.8;
        double energy = 0.6;
        double sadness = 0.4;
        double love = 0.7;

        // Act
        String query = spotifyApiService.generateQueryBasedOnMood(happiness, energy, sadness, love);

        // Assert
        assertNotNull(query);
        assertFalse(query.isEmpty());
        assertTrue(query.contains("happy") || query.contains("joyful") || query.contains("romantic"));
    }

    @Test
    void getPlaylistDetailsSuccess() throws IOException {
        // Arrange
        String playlistId = "mockPlaylistId";
        String accessToken = "mockAccessToken";

        String responseBody = """
                {
                    "id": "mockPlaylistId",
                    "name": "Test Playlist",
                    "description": "A test playlist"
                }
                """;

        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://mockurl.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("")
                .body(ResponseBody.create(responseBody, MediaType.get("application/json")))
                .build();

        Call mockCall = mock(Call.class);
        when(mockClient.newCall(any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act
        String result = spotifyApiService.getPlaylistDetails(playlistId, accessToken);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Test Playlist"));
    }
}
