package com.ubb.zenith.service;

import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.PlaylistRepository;
import com.ubb.zenith.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaylistServiceTest {

    @InjectMocks
    private PlaylistService playlistService;

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePlaylistSuccess() {
        // Arrange
        Integer userId = 1;
        String name = "Chill Vibes";
        String mood = "Relaxed";
        String spotifyPlaylistId = "12345";

        User mockUser = new User();
        mockUser.setId(userId);

        Playlist mockPlaylist = new Playlist();
        mockPlaylist.setName(name);
        mockPlaylist.setMood(mood);
        mockPlaylist.setSpotifyPlaylistId(spotifyPlaylistId);
        mockPlaylist.setUser(mockUser);
        mockPlaylist.setCreatedAt(LocalDate.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(playlistRepository.save(any(Playlist.class))).thenReturn(mockPlaylist);

        // Act
        Playlist result = playlistService.savePlaylist(userId, name, mood, spotifyPlaylistId);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(mood, result.getMood());
        verify(userRepository, times(1)).findById(userId);
        verify(playlistRepository, times(1)).save(any(Playlist.class));
    }

    @Test
    void savePlaylistUserNotFound() {
        // Arrange
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            playlistService.savePlaylist(userId, "Chill Vibes", "Relaxed", "12345");
        });
        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(playlistRepository, never()).save(any(Playlist.class));
    }

    @Test
    void findPlaylistsByUsername() {
        // Arrange
        String username = "testUser";
        List<Playlist> mockPlaylists = new ArrayList<>();
        mockPlaylists.add(new Playlist());
        when(playlistRepository.findByUserUsername(username)).thenReturn(mockPlaylists);

        // Act
        List<Playlist> result = playlistService.findPlaylistsByUsername(username);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(playlistRepository, times(1)).findByUserUsername(username);
    }

    @Test
    void getUserPlaylists() {
        // Arrange
        Integer userId = 1;
        List<Playlist> mockPlaylists = new ArrayList<>();
        mockPlaylists.add(new Playlist());
        when(playlistRepository.findByUserId(userId)).thenReturn(mockPlaylists);

        // Act
        List<Playlist> result = playlistService.getUserPlaylists(userId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(playlistRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getPlaylistSuccess() {
        // Arrange
        Integer playlistId = 1;
        Playlist mockPlaylist = new Playlist();
        mockPlaylist.setId(playlistId);
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(mockPlaylist));

        // Act
        Optional<Playlist> result = playlistService.getPlaylist(playlistId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(playlistId, result.get().getId());
        verify(playlistRepository, times(1)).findById(playlistId);
    }

    @Test
    void getPlaylistNotFound() {
        // Arrange
        Integer playlistId = 1;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        // Act
        Optional<Playlist> result = playlistService.getPlaylist(playlistId);

        // Assert
        assertFalse(result.isPresent());
        verify(playlistRepository, times(1)).findById(playlistId);
    }

    @Test
    void updatePlaylistSuccess() {
        // Arrange
        Integer playlistId = 1;
        String newName = "Updated Name";
        String newMood = "Energetic";
        String newSpotifyPlaylistId = "67890";

        Playlist mockPlaylist = new Playlist();
        mockPlaylist.setId(playlistId);

        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(mockPlaylist));

        // Act
        Playlist result = playlistService.updatePlaylist(playlistId, newName, newMood, newSpotifyPlaylistId);

        // Assert
        assertNotNull(result);
        assertEquals(newName, result.getName());
        assertEquals(newMood, result.getMood());
        verify(playlistRepository, times(1)).findById(playlistId);
    }

    @Test
    void updatePlaylistNotFound() {
        // Arrange
        Integer playlistId = 1;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            playlistService.updatePlaylist(playlistId, "Updated Name", "Energetic", "67890");
        });
        assertEquals("Playlist not found with id: 1", exception.getMessage());
        verify(playlistRepository, times(1)).findById(playlistId);
    }

    @Test
    void deletePlaylistSuccess() {
        // Arrange
        Integer playlistId = 1;
        Playlist mockPlaylist = new Playlist();
        mockPlaylist.setId(playlistId);

        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(mockPlaylist));
        doNothing().when(playlistRepository).delete(mockPlaylist);

        // Act
        playlistService.deletePlaylist(playlistId);

        // Assert
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(playlistRepository, times(1)).delete(mockPlaylist);
    }

    @Test
    void deletePlaylistNotFound() {
        // Arrange
        Integer playlistId = 1;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            playlistService.deletePlaylist(playlistId);
        });
        assertEquals("Playlist not found with id: 1", exception.getMessage());
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(playlistRepository, never()).delete(any(Playlist.class));
    }
}
