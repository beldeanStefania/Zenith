package com.ubb.zenith.controller;

import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.PlaylistRepository;
import com.ubb.zenith.repository.UserRepository;
import com.ubb.zenith.service.PlaylistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


//@RunWith(MockitoJUnitRunner.class)
public class PlaylistControllerTest {

    @Mock
    private PlaylistService playlistService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlaylistRepository playlistRepository;

    @InjectMocks
    private PlaylistController playlistController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Creează o nouă instanță a PlaylistController
        playlistController = new PlaylistController(playlistService);

        // Setați manual valorile pentru userRepository și playlistRepository folosind reflecție
        setField(playlistController, "userRepository", userRepository);
        setField(playlistController, "playlistRepository", playlistRepository);
    }

    // Utilitar pentru a seta câmpuri private folosind reflecție
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Permite accesarea câmpului privat
        field.set(target, value); // Setează valoarea câmpului
    }


    @Test
    public void addPlaylist() {
        // Arrange
        String username = "testUser";
        String name = "Chill Vibes";
        String mood = "Relaxed";
        String spotifyPlaylistId = "12345";

        User mockUser = new User();
        mockUser.setUsername(username);

        Playlist mockPlaylist = new Playlist();
        mockPlaylist.setName(name);
        mockPlaylist.setMood(mood);
        mockPlaylist.setSpotifyPlaylistId(spotifyPlaylistId);
        mockPlaylist.setUser(mockUser);
        mockPlaylist.setCreatedAt(LocalDate.now());

        // Configurează comportamentul mock-urilor
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(playlistRepository.save(any(Playlist.class))).thenReturn(mockPlaylist);

        // Act
        ResponseEntity<Playlist> response = playlistController.addPlaylist(username, name, mood, spotifyPlaylistId);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(name, response.getBody().getName());
        assertEquals(mood, response.getBody().getMood());
        verify(userRepository, times(1)).findByUsername(username);
        verify(playlistRepository, times(1)).save(any(Playlist.class));
    }

    @Test
    public void getUserPlaylists() {
        // Arrange
        String username = "testUser";
        List<Playlist> mockPlaylists = new ArrayList<>();
        mockPlaylists.add(new Playlist());
        when(playlistService.findPlaylistsByUsername(username)).thenReturn(mockPlaylists);

        // Act
        ResponseEntity<List<Playlist>> response = playlistController.getUserPlaylists(username);

        // Assert
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(playlistService, times(1)).findPlaylistsByUsername(username);
    }

    @Test
    public void testGetUserPlaylistsByUserId() {
        // Arrange
        Integer userId = 1;
        List<Playlist> mockPlaylists = new ArrayList<>();
        mockPlaylists.add(new Playlist());
        when(playlistService.getUserPlaylists(userId)).thenReturn(mockPlaylists);

        // Act
        ResponseEntity<List<Playlist>> response = playlistController.getUserPlaylists(userId);

        // Assert
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(playlistService, times(1)).getUserPlaylists(userId);
    }

    @Test
    public void getPlaylist() {
        // Arrange
        Integer playlistId = 1;
        Playlist mockPlaylist = new Playlist();
        mockPlaylist.setId(playlistId);
        when(playlistService.getPlaylist(playlistId)).thenReturn(Optional.of(mockPlaylist));

        // Act
        ResponseEntity<Playlist> response = playlistController.getPlaylist(playlistId);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(playlistId, response.getBody().getId());
        verify(playlistService, times(1)).getPlaylist(playlistId);
    }

    @Test
    public void updatePlaylist() {
        // Arrange
        Integer playlistId = 1;
        String name = "Updated Name";
        String mood = "Happy";
        String spotifyPlaylistId = "67890";

        Playlist updatedPlaylist = new Playlist();
        updatedPlaylist.setId(playlistId);
        updatedPlaylist.setName(name);
        updatedPlaylist.setMood(mood);
        updatedPlaylist.setSpotifyPlaylistId(spotifyPlaylistId);

        when(playlistService.updatePlaylist(playlistId, name, mood, spotifyPlaylistId)).thenReturn(updatedPlaylist);

        // Act
        ResponseEntity<Playlist> response = playlistController.updatePlaylist(playlistId, name, mood, spotifyPlaylistId);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(name, response.getBody().getName());
        assertEquals(mood, response.getBody().getMood());
        verify(playlistService, times(1)).updatePlaylist(playlistId, name, mood, spotifyPlaylistId);
    }

    @Test
    public void deletePlaylist() {
        // Arrange
        Integer playlistId = 1;

        // Act
        ResponseEntity<String> response = playlistController.deletePlaylist(playlistId);

        // Assert
        assertEquals("Playlist deleted successfully.", response.getBody());
        verify(playlistService, times(1)).deletePlaylist(playlistId);
    }
}