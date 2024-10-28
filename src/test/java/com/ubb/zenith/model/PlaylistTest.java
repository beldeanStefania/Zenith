package com.ubb.zenith.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaylistTest {

    private Playlist playlist;
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        playlist = new Playlist();
    }

    @AfterEach
    void tearDown() {
        playlist = null;
    }

    // Teste pentru ID
    @Test
    void getId() {
        playlist.setId(1);
        assertEquals(1, playlist.getId());
    }

    @Test
    void setId() {
        playlist.setId(2);
        assertEquals(2, playlist.getId());
    }

    // Teste pentru numele playlist-ului
    @Test
    void getName() {
        playlist.setName("Chill Vibes");
        assertEquals("Chill Vibes", playlist.getName());
    }

    @Test
    void setName() {
        playlist.setName("Workout Hits");
        assertEquals("Workout Hits", playlist.getName());
    }

    @Test
    void testNameTooShort() {
        playlist.setName("AB"); // prea scurt
        Set<ConstraintViolation<Playlist>> violations = validator.validate(playlist);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Playlist name must be between 3 and 20 characters")));
    }

    @Test
    void testNameEmpty() {
        playlist.setName("");
        Set<ConstraintViolation<Playlist>> violations = validator.validate(playlist);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Playlist name cannot be empty")));
    }

    // Teste pentru lista de melodii (songs)
    @Test
    void setSongs() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song(1, "Song 1", "Artist 1", "Pop", playlist, null));
        songs.add(new Song(2, "Song 2", "Artist 2", "Rock", playlist, null));
        playlist.setSongs(songs);

        assertEquals(2, playlist.getSongs().size());
        assertEquals("Song 1", playlist.getSongs().get(0).getTitle());
        assertEquals("Song 2", playlist.getSongs().get(1).getTitle());
    }

    @Test
    void getSongs() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song(1, "Song 1", "Artist 1", "Pop", playlist, null));
        playlist.setSongs(songs);

        assertEquals(1, playlist.getSongs().size());
        assertEquals("Song 1", playlist.getSongs().get(0).getTitle());
    }

    @Test
    void testEmptySongsList() {
        playlist.setSongs(new ArrayList<>());
        assertEquals(0, playlist.getSongs().size(), "Lista de melodii ar trebui să fie goală.");
    }
}
