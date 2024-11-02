package com.ubb.zenith.service;

import com.ubb.zenith.dto.PlaylistDTO;
import com.ubb.zenith.exception.PlaylistAlreadyExistsException;
import com.ubb.zenith.exception.PlaylistNotFoundException;
import com.ubb.zenith.exception.SongAlreadyExistsException;
import com.ubb.zenith.exception.SongNotFoundException;
import com.ubb.zenith.model.Mood;
import com.ubb.zenith.model.Playlist;
import com.ubb.zenith.model.Song;
import com.ubb.zenith.repository.MoodRepository;
import com.ubb.zenith.repository.PlaylistRepository;
import com.ubb.zenith.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private SongRepository songRepository;

    @Mock
    private MoodRepository moodRepository;

    @InjectMocks
    private PlaylistService playlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        when(playlistRepository.findAll()).thenReturn(List.of(new Playlist()));
        List<Playlist> playlists = playlistService.getAll();
        assertFalse(playlists.isEmpty());
        verify(playlistRepository, times(1)).findAll();
    }

    @Test
    void add() throws PlaylistAlreadyExistsException {
        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setName("Chill Playlist");

        when(playlistRepository.findByName(playlistDTO.getName())).thenReturn(Optional.empty());
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Playlist playlist = playlistService.add(playlistDTO);
        assertEquals("Chill Playlist", playlist.getName());
        verify(playlistRepository, times(1)).findByName(playlistDTO.getName());
        verify(playlistRepository, times(1)).save(any(Playlist.class));
    }

    @Test
    void addThrowsPlaylistAlreadyExistsException() {
        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setName("Existing Playlist");

        when(playlistRepository.findByName(playlistDTO.getName())).thenReturn(Optional.of(new Playlist()));

        assertThrows(PlaylistAlreadyExistsException.class, () -> playlistService.add(playlistDTO));
        verify(playlistRepository, times(1)).findByName(playlistDTO.getName());
    }

    @Test
    void update() throws PlaylistNotFoundException {
        String oldName = "Old Playlist";
        PlaylistDTO newPlaylistData = new PlaylistDTO();
        newPlaylistData.setName("Updated Playlist");

        Playlist existingPlaylist = new Playlist();
        existingPlaylist.setName(oldName);

        when(playlistRepository.findByName(oldName)).thenReturn(Optional.of(existingPlaylist));
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Playlist updatedPlaylist = playlistService.update(oldName, newPlaylistData);
        assertEquals("Updated Playlist", updatedPlaylist.getName());
        verify(playlistRepository, times(1)).findByName(oldName);
        verify(playlistRepository, times(1)).save(existingPlaylist);
    }

    @Test
    void updateThrowsPlaylistNotFoundException() {
        String oldName = "Nonexistent Playlist";
        PlaylistDTO newPlaylistData = new PlaylistDTO();

        when(playlistRepository.findByName(oldName)).thenReturn(Optional.empty());

        assertThrows(PlaylistNotFoundException.class, () -> playlistService.update(oldName, newPlaylistData));
        verify(playlistRepository, times(1)).findByName(oldName);
    }

    @Test
    void addSongToPlaylist() throws PlaylistNotFoundException, SongNotFoundException, SongAlreadyExistsException {
        String playlistName = "Chill Playlist";
        int songId = 1;

        Playlist playlist = new Playlist();
        playlist.setSongs(new ArrayList<>()); // Inițializează lista cu o listă goală

        Song song = new Song();
        song.setId(songId);

        when(playlistRepository.findByName(playlistName)).thenReturn(Optional.of(playlist));
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(playlistRepository.findBySongs(song)).thenReturn(Optional.empty());
        when(playlistRepository.save(playlist)).thenAnswer(invocation -> invocation.getArgument(0));

        Playlist updatedPlaylist = playlistService.addSongToPlaylist(playlistName, songId);
        assertTrue(updatedPlaylist.getSongs().contains(song));
        verify(songRepository, times(1)).save(song);
        verify(playlistRepository, times(1)).save(playlist);
    }


    @Test
    void delete() throws PlaylistNotFoundException {
        String name = "Chill Playlist";
        Playlist playlist = new Playlist();
        playlist.setName(name);

        when(playlistRepository.findByName(name)).thenReturn(Optional.of(playlist));

        playlistService.delete(name);

        verify(playlistRepository, times(1)).findByName(name);
        verify(playlistRepository, times(1)).delete(playlist);
    }

    @Test
    void generatePlaylistForUser() throws PlaylistAlreadyExistsException {
        int happinessScore = 7;
        int sadnessScore = 3;
        int loveScore = 6;
        int energyScore = 5;
        String playlistName = "Mood Playlist";

        Mood mood = new Mood();
        mood.setHappiness_score(7);
        mood.setSadness_score(3);
        mood.setLove_score(6);
        mood.setEnergy_score(5);

        Song song = new Song();
        song.setTitle("Happy Song");

        mood.setSongs(List.of(song));

        when(moodRepository.findAll()).thenReturn(List.of(mood));
        when(playlistRepository.findByName(playlistName)).thenReturn(Optional.empty());
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Playlist generatedPlaylist = playlistService.generatePlaylistForUser(happinessScore, sadnessScore, loveScore, energyScore, playlistName);
        assertEquals(playlistName, generatedPlaylist.getName());
        assertTrue(generatedPlaylist.getSongs().contains(song));
        verify(playlistRepository, times(1)).save(any(Playlist.class));
    }
}
