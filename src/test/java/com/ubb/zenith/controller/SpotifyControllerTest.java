package com.ubb.zenith.controller;

import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.UserRepository;
import com.ubb.zenith.service.SpotifyApiService;
import com.ubb.zenith.service.SpotifyAuthService;
import com.ubb.zenith.service.UserService;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SpotifyControllerTest {

    @InjectMocks
    private SpotifyController spotifyController;

    @Mock
    private SpotifyAuthService spotifyAuthService;

    @Mock
    private SpotifyApiService spotifyApiService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginToSpotify() {
        // Arrange
        String username = "testUser";
        String expectedUrl = "https://spotify.com/login";
        when(spotifyAuthService.getSpotifyAuthorizationUrl(username)).thenReturn(expectedUrl);

        // Act
        ResponseEntity<String> response = spotifyController.loginToSpotify(username);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedUrl, response.getBody());
        verify(spotifyAuthService, times(1)).getSpotifyAuthorizationUrl(username);
    }

    @Test
    void authenticateWithSpotifySuccess() throws Exception {
        // Arrange
        String username = "testUser";
        String code = "authCode";
        User mockUser = new User();
        mockUser.setUsername(username);
        JSONObject tokens = new JSONObject();
        tokens.put("access_token", "mockAccessToken");
        tokens.put("refresh_token", "mockRefreshToken");
        tokens.put("expires_in", 3600);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(spotifyAuthService.exchangeCodeForToken(code)).thenReturn(tokens);

        // Act
        ResponseEntity<String> response = spotifyController.authenticateWithSpotify(username, code);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User authenticated and tokens saved successfully!", response.getBody());
        verify(userRepository, times(1)).findByUsername(username);
        verify(spotifyAuthService, times(1)).exchangeCodeForToken(code);
        verify(spotifyAuthService, times(1)).saveTokens(username, "mockAccessToken", "mockRefreshToken", 3600);
    }

    @Test
    void authenticateWithSpotifyUserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        String code = "authCode";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = spotifyController.authenticateWithSpotify(username, code);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error during authentication: User not found: nonExistentUser", response.getBody());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void generatePlaylistFromMoodSuccess() throws Exception {
        // Arrange
        String username = "testUser";
        String playlistName = "My Mood Playlist";
        String mockAccessToken = "mockAccessToken";
        String query = "happy energetic songs";
        String playlistUrl = "https://open.spotify.com/playlist/mockPlaylistId";

        when(userService.getAccessToken(eq(username))).thenReturn(mockAccessToken);
        when(spotifyApiService.generateQueryBasedOnMood(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(query);
        when(spotifyApiService.searchTracks(eq(query), anyDouble(), anyDouble(), eq(mockAccessToken)))
                .thenReturn(Collections.singletonList("mockTrackUri"));
        when(spotifyApiService.getCurrentUserId(eq(mockAccessToken))).thenReturn("mockUserId");
        when(spotifyApiService.createPlaylist(eq("mockUserId"), eq(playlistName), anyString(), eq(true), eq(mockAccessToken)))
                .thenReturn("mockPlaylistId");

        // Act
        ResponseEntity<String> response = spotifyController.generatePlaylistFromMood(username, 5, 5, 3, 4, playlistName);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(playlistUrl, response.getBody());
        verify(spotifyApiService, times(1)).createPlaylist(eq("mockUserId"), eq(playlistName), anyString(), eq(true), eq(mockAccessToken));
    }


    @SneakyThrows
    @Test
    void generatePlaylistFromMoodUserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userService.getAccessToken(username)).thenThrow(new UserNotFoundException("User not found"));

        // Act
        ResponseEntity<String> response = spotifyController.generatePlaylistFromMood(username, 5, 5, 3, 4, "Playlist");

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found: nonExistentUser", response.getBody());
    }

    @Test
    void viewPlaylistSuccess() throws Exception {
        // Arrange
        String username = "testUser";
        String playlistId = "mockPlaylistId";
        String mockAccessToken = "mockAccessToken";
        String playlistDetails = "{ \"name\": \"Test Playlist\" }";

        when(userService.getAccessToken(username)).thenReturn(mockAccessToken);
        when(spotifyApiService.getPlaylistDetails(playlistId, mockAccessToken)).thenReturn(playlistDetails);

        // Act
        ResponseEntity<String> response = spotifyController.viewPlaylist(username, playlistId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(playlistDetails, response.getBody());
        verify(spotifyApiService, times(1)).getPlaylistDetails(playlistId, mockAccessToken);
    }

    @Test
    void playPlaylistSuccess() throws Exception {
        // Arrange
        String username = "testUser";
        String playlistId = "https://open.spotify.com/playlist/mockPlaylistId";
        String mockAccessToken = "mockAccessToken";

        when(userService.getAccessToken(username)).thenReturn(mockAccessToken);
        when(spotifyApiService.getTracksFromPlaylist(anyString(), anyString())).thenReturn(Collections.singletonList("mockTrackUri"));

        // Act
        ResponseEntity<String> response = spotifyController.playPlaylist(username, playlistId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Playlist playback started successfully!", response.getBody());
        verify(spotifyApiService, times(1)).getTracksFromPlaylist(anyString(), anyString());
        verify(spotifyApiService, times(1)).playTracks(anyList(), anyString());
    }
}
