package com.ubb.zenith.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {

    @Test
    public void testSetAndGetTitle() {
        Song song = new Song();
        song.setTitle("Shape of You"); // Setting a valid title
        assertEquals("Shape of You", song.getTitle());
    }

    @Test
    public void testSetAndGetArtist() {
        Song song = new Song();
        song.setArtist("Ed Sheeran"); // Setting a valid artist
        assertEquals("Ed Sheeran", song.getArtist());
    }

    @Test
    public void testSetAndGetGenre() {
        Song song = new Song();
        song.setGenre("Pop"); // Setting a valid genre
        assertEquals("Pop", song.getGenre());
    }

    @Test
    public void testSetAndGetMood() {
        Mood mood = new Mood();
        mood.setHappiness_score(5);
        mood.setSadness_score(3);
        mood.setLove_score(4);
        mood.setEnergy_score(6);

        Song song = new Song();
        song.setMood(mood); // Setting a valid mood
        assertEquals(mood, song.getMood());
    }

}
