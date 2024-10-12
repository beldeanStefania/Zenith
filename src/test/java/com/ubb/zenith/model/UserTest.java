package com.ubb.zenith.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    @Test
    void getId() {
        user.setId(1);
        assertEquals(1, user.getId());
    }

    @Test
    void getUsername() {
        user.setUsername("JohnDoe");
        assertEquals("JohnDoe", user.getUsername());
    }

    @Test
    void setId() {
        user.setId(2);
        assertEquals(2, user.getId());
    }

    @Test
    void setUsername() {
        user.setUsername("JaneDoe");
        assertEquals("JaneDoe", user.getUsername());
    }
}