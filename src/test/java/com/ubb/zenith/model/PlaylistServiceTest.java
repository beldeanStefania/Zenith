package com.ubb.zenith.model;

import com.ubb.zenith.dto.PlaylistDTO;
import com.ubb.zenith.exception.PlaylistAlreadyExistsException;

import com.ubb.zenith.exception.PlaylistNotFoundException;
import com.ubb.zenith.repository.PlaylistRepository;
import com.ubb.zenith.repository.SongRepository;
import com.ubb.zenith.service.PlaylistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private PlaylistService playlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addSongToPlaylist_AddsSongSuccessfully() throws PlaylistNotFoundException {
        // Arrange
        Playlist playlist = new Playlist();
        playlist.setName("My Playlist");
        playlist.setSongs(new ArrayList<>());

        Song song = new Song();
        song.setId(1);
        song.setTitle("My Song");

        // Mocking: Când căutăm playlist-ul și melodia, returnăm datele noastre simulate
        when(playlistRepository.findAll()).thenReturn(List.of(playlist));
        when(songRepository.findById(1)).thenReturn(Optional.of(song));
        when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

        // Act: Adăugăm melodia în playlist
        playlistService.addSongToPlaylist("My Playlist", 1);

        // Assert: Verificăm că melodia a fost adăugată corect
        assertTrue(playlist.getSongs().contains(song));
        verify(playlistRepository, times(1)).save(playlist);
    }

    @Test
    void getAll_ReturnsListOfPlaylists() {
        // Arrange
        Playlist playlist1 = new Playlist();
        playlist1.setName("Playlist 1");

        Playlist playlist2 = new Playlist();
        playlist2.setName("Playlist 2");

        when(playlistRepository.findAll()).thenReturn(Arrays.asList(playlist1, playlist2));

        // Act
        List<Playlist> playlists = playlistService.getAll();

        // Assert
        assertEquals(2, playlists.size());
        assertEquals("Playlist 1", playlists.get(0).getName());
        assertEquals("Playlist 2", playlists.get(1).getName());
    }

    @Test
    void add_ThrowsException_WhenPlaylistAlreadyExists() {
        // Arrange
        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setName("Existing Playlist");

        when(playlistRepository.findByName(playlistDTO.getName())).thenReturn(Optional.of(new Playlist()));

        // Act & Assert
        assertThrows(PlaylistAlreadyExistsException.class, () -> playlistService.add(playlistDTO));
    }

    @Test
    void add_AddsPlaylistSuccessfully_WhenPlaylistDoesNotExist() throws PlaylistAlreadyExistsException {
        // Arrange
        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setName("New Playlist");

        when(playlistRepository.findByName(playlistDTO.getName())).thenReturn(Optional.empty());
        when(playlistRepository.save(any(Playlist.class))).thenReturn(new Playlist());

        // Act
        Playlist addedPlaylist = playlistService.add(playlistDTO);

        // Assert
        assertNotNull(addedPlaylist);
        verify(playlistRepository, times(1)).save(any(Playlist.class));
    }

    @Test
    void update_ThrowsException_WhenPlaylistNotFound() {
        // Arrange
        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setName("Non-Existent Playlist");

        when(playlistRepository.findAll()).thenReturn(List.of());

        // Act & Assert
        assertThrows(PlaylistNotFoundException.class, () -> playlistService.update("Non-Existent Playlist", playlistDTO));
    }

    @Test
    void delete_DeletesPlaylistSuccessfully_WhenPlaylistExists() throws PlaylistNotFoundException {
        // Arrange
        Playlist playlist = new Playlist();
        playlist.setName("Playlist To Delete");

        when(playlistRepository.findAll()).thenReturn(List.of(playlist));

        // Act
        playlistService.delete("Playlist To Delete");

        // Assert
        verify(playlistRepository, times(1)).delete(any(Playlist.class));
    }
}
