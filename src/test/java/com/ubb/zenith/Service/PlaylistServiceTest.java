package com.ubb.zenith.Service;
import com.ubb.zenith.dto.PlaylistDTO;
import com.ubb.zenith.exception.PlaylistAlreadyExistsException;
import com.ubb.zenith.exception.PlaylistNotFoundException;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.Song;
import com.ubb.zenith.repository.PlaylistRepository;
import com.ubb.zenith.repository.SongRepository;
import com.ubb.zenith.service.PlaylistService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    private SongRepository songRepository;

    private Playlist playlist;
    private PlaylistDTO playlistDTO;
    private Song song;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("My Playlist");
        playlist.setSongs(new ArrayList<>());

        playlistDTO = new PlaylistDTO();
        playlistDTO.setName("My Playlist");
        playlistDTO.setId_song(1);

        song = new Song();
        song.setId(1);
        song.setTitle("My Song");
    }

    @AfterEach
    void tearDown() {
        // Clean up resources if needed
    }

    @Test
    void testGetAll() {
        List<Playlist> playlists = List.of(playlist);
        when(playlistRepository.findAll()).thenReturn(playlists);

        List<Playlist> result = playlistService.getAll();

        assertEquals(1, result.size());
        assertEquals("My Playlist", result.get(0).getName());
    }

//    @Test
//    void testAddPlaylist_Success() throws PlaylistAlreadyExistsException {
//        when(playlistRepository.findByName(playlistDTO.getName())).thenReturn(Optional.empty());
//
//        Playlist result = playlistService.add(playlistDTO);
//
//        assertNotNull(result);
//        assertEquals("My Playlist", result.getName());
//        verify(playlistRepository).save(any(Playlist.class));
//    }

    @Test
    void testAddPlaylist_Success() throws PlaylistAlreadyExistsException {
        when(playlistRepository.findByName(playlistDTO.getName())).thenReturn(Optional.empty());
        when(songRepository.findAll()).thenReturn(List.of(song)); // Mocking song filtering in buildPlaylist
        when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist); // Explicitly mock save with returned Playlist

        Playlist result = playlistService.add(playlistDTO);

        assertNotNull(result);
        assertEquals("My Playlist", result.getName());
        verify(playlistRepository).save(any(Playlist.class));
    }


    @Test
    void testAddPlaylist_AlreadyExists() {
        when(playlistRepository.findByName(playlistDTO.getName())).thenReturn(Optional.of(playlist));

        assertThrows(PlaylistAlreadyExistsException.class, () -> playlistService.add(playlistDTO));
    }

//    @Test
//    void testUpdatePlaylist_Success() throws PlaylistNotFoundException {
//        when(playlistRepository.findByName("My Playlist")).thenReturn(Optional.of(playlist));
//
//        playlistDTO.setName("Updated Playlist");
//        Playlist updatedPlaylist = playlistService.update("My Playlist", playlistDTO);
//
//        assertEquals("Updated Playlist", updatedPlaylist.getName());
//        verify(playlistRepository).save(playlist);
//    }
@Test
void testUpdatePlaylist_Success() throws PlaylistNotFoundException {
    // Crearea playlistului inițial
    Playlist playlist = new Playlist();
    playlist.setName("My Playlist");

    // Crearea obiectului PlaylistDTO cu noul nume
    PlaylistDTO playlistDTO = new PlaylistDTO();
    playlistDTO.setName("Updated Playlist");

    // Setarea comportamentului mock
    when(playlistRepository.findByName("My Playlist")).thenReturn(Optional.of(playlist));
    when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

    // Executarea metodei de update
    Playlist updatedPlaylist = playlistService.update("My Playlist", playlistDTO);

    // Verificarea rezultatului
    assertNotNull(updatedPlaylist);
    assertEquals("Updated Playlist", updatedPlaylist.getName());
    verify(playlistRepository).save(playlist);
}


    @Test
    void testUpdatePlaylist_NotFound() {
        when(playlistRepository.findByName("My Playlist")).thenReturn(Optional.empty());

        assertThrows(PlaylistNotFoundException.class, () -> playlistService.update("My Playlist", playlistDTO));
    }

    @Test
    void testFindPlaylist_Success() throws PlaylistNotFoundException {
        when(playlistRepository.findByName("My Playlist")).thenReturn(Optional.of(playlist));

        Playlist result = playlistService.findPlaylist("My Playlist");

        assertNotNull(result);
        assertEquals("My Playlist", result.getName());
    }

    @Test
    void testFindPlaylist_NotFound() {
        when(playlistRepository.findByName("My Playlist")).thenReturn(Optional.empty());

        assertThrows(PlaylistNotFoundException.class, () -> playlistService.findPlaylist("My Playlist"));
    }

    @Test
    void testAddSongToPlaylist_Success() throws PlaylistNotFoundException {
        when(playlistRepository.findByName("My Playlist")).thenReturn(Optional.of(playlist));
        when(songRepository.findById(1)).thenReturn(Optional.of(song));

        playlistService.addSongToPlaylist("My Playlist", 1);

        assertTrue(playlist.getSongs().contains(song));
        verify(playlistRepository).save(playlist);
    }

    @Test
    void testAddSongToPlaylist_PlaylistNotFound() {
        when(playlistRepository.findByName("My Playlist")).thenReturn(Optional.empty());

        assertThrows(PlaylistNotFoundException.class, () -> playlistService.addSongToPlaylist("My Playlist", 1));
    }

    @Test
    void testDeletePlaylist_Success() throws PlaylistNotFoundException {
        when(playlistRepository.findByName("My Playlist")).thenReturn(Optional.of(playlist));

        playlistService.delete("My Playlist");

        verify(playlistRepository).delete(playlist);
    }

    @Test
    void testDeletePlaylist_NotFound() {
        when(playlistRepository.findByName("My Playlist")).thenReturn(Optional.empty());

        assertThrows(PlaylistNotFoundException.class, () -> playlistService.delete("My Playlist"));
    }
}
