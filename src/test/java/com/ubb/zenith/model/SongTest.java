package com.ubb.zenith.model;

import com.ubb.zenith.model.Mood;
import com.ubb.zenith.model.Song;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SongTest {

    private Song song;

    @BeforeEach
    void setUp() {
        song = new Song();
    }

    @AfterEach
    void tearDown() {
        song = null;
    }

    @Test
    void testGetId() {
        song.setId(1);
        assertEquals(1, song.getId());
    }

    @Test
    void testSetId() {
        song.setId(2);
        assertEquals(2, song.getId());
    }

    @Test
    void testGetTitle() {
        song.setTitle("Imagine");
        assertEquals("Imagine", song.getTitle());
    }

    @Test
    void testSetTitle() {
        song.setTitle("Bohemian Rhapsody");
        assertEquals("Bohemian Rhapsody", song.getTitle());
    }

    @Test
    void testGetArtist() {
        song.setArtist("The Beatles");
        assertEquals("The Beatles", song.getArtist());
    }

    @Test
    void testSetArtist() {
        song.setArtist("Queen");
        assertEquals("Queen", song.getArtist());
    }

    @Test
    void testGetGenre() {
        song.setGenre("Rock");
        assertEquals("Rock", song.getGenre());
    }

    @Test
    void testSetGenre() {
        song.setGenre("Pop");
        assertEquals("Pop", song.getGenre());
    }

    @Test
    void testGetMood() {
        Mood mood = new Mood();
        song.setMood(mood);
        assertEquals(mood, song.getMood());
    }

    @Test
    void testSetMood() {
        Mood mood = new Mood();
        song.setMood(mood);
        assertNotNull(song.getMood());
        assertEquals(mood, song.getMood());
    }
}
