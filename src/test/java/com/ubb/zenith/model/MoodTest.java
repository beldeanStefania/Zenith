//package com.ubb.zenith.model;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class MoodTest {
//
//    @Test
//    public void testSetAndGetHappinessScore() {
//        Mood mood = new Mood();
//        mood.setHappiness_score(5); // Setting a valid score
//        assertEquals(5, mood.getHappiness_score());
//    }
//
//    @Test
//    public void testSetAndGetSadnessScore() {
//        Mood mood = new Mood();
//        mood.setSadness_score(6); // Setting a valid score
//        assertEquals(6, mood.getSadness_score());
//    }
//
//    @Test
//    public void testSetAndGetLoveScore() {
//        Mood mood = new Mood();
//        mood.setLove_score(7); // Setting a valid score
//        assertEquals(7, mood.getLove_score());
//    }
//
//    @Test
//    public void testSetAndGetEnergyScore() {
//        Mood mood = new Mood();
//        mood.setEnergy_score(8); // Setting a valid score
//        assertEquals(8, mood.getEnergy_score());
//    }
//
//    @Test
//    public void testSetAndGetAllScores() {
//        Mood mood = new Mood();
//        mood.setHappiness_score(4);
//        mood.setSadness_score(3);
//        mood.setLove_score(5);
//        mood.setEnergy_score(6);
//
//        assertEquals(4, mood.getHappiness_score());
//        assertEquals(3, mood.getSadness_score());
//        assertEquals(5, mood.getLove_score());
//        assertEquals(6, mood.getEnergy_score());
//    }
//
//    @Test
//    public void testSongsList() {
//        Mood mood = new Mood();
//        assertNull(mood.getSongs()); // Ensure songs list is initially null
//
//        // You can also test initializing the songs list
//        mood.setSongs(new java.util.ArrayList<>());
//        assertNotNull(mood.getSongs()); // Ensure songs list is not null after initialization
//        assertTrue(mood.getSongs().isEmpty()); // Ensure songs list is empty initially
//    }
//}
