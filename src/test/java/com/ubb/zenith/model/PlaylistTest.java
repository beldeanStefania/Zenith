package com.ubb.zenith.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {
    private Playlist playlist;
    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
            playlist = new Playlist();
    }

    @AfterEach
    void tearDown() {
        playlist = null;
    }

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

    @Test
    void getSongs() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song());
        playlist.setSongs(songs);
        assertEquals(songs, playlist.getSongs());
    }

    @Test
    void setSongs() {
        List<Song> songs = new ArrayList<>();
        Song song = new Song();
        songs.add(song);
        playlist.setSongs(songs);
        assertEquals(1, playlist.getSongs().size());
        assertTrue(playlist.getSongs().contains(song));
    }

    @Test
    void getName() {
        playlist.setName("My Playlist");
        assertEquals("My Playlist", playlist.getName());
    }

    @Test
    void setName() {
        playlist.setName("Another Playlist");
        assertEquals("Another Playlist", playlist.getName());
    }

    @Test
    void testNameValidation() {
        // Test empty name
        playlist.setName("");
        Set<ConstraintViolation<Playlist>> violations = validator.validate(playlist);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertFalse(violations.isEmpty());

        // Test short name
        playlist.setName("AB");
        violations = validator.validate(playlist);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertFalse(violations.isEmpty());

        // Test valid name
        playlist.setName("Valid Name");
        violations = validator.validate(playlist);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertTrue(violations.isEmpty());

        // Test name too long
        playlist.setName("A very long playlist name exceeding limit");
        violations = validator.validate(playlist);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertFalse(violations.isEmpty());
    }
}