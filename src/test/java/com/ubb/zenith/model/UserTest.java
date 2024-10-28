package com.ubb.zenith.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private User user;
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    // Teste pentru ID
    @Test
    void getId() {
        user.setId(1);
        assertEquals(1, user.getId());
    }

    @Test
    void setId() {
        user.setId(2);
        assertEquals(2, user.getId());
    }

    // Teste pentru Username
    @Test
    void getUsername() {
        user.setUsername("JohnDoe");
        assertEquals("JohnDoe", user.getUsername());
    }

    @Test
    void setUsername() {
        user.setUsername("JaneDoe");
        assertEquals("JaneDoe", user.getUsername());
    }

    @Test
    void testUsernameTooShort() {
        user.setUsername("ab");  // prea scurt
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Username must be between 3 and 20 characters")));
    }

    @Test
    void testUsernameEmpty() {
        user.setUsername("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Username cannot be empty")));
    }

    // Teste pentru Email
    @Test
    void setEmail() {
        user.setEmail("valid.email@example.com");
        assertEquals("valid.email@example.com", user.getEmail());
    }

    @Test
    void testEmailEmpty() {
        user.setEmail("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Email cannot be empty")));
    }

    // Teste pentru Password
    @Test
    void setPassword() {
        user.setPassword("Valid@123");
        assertEquals("Valid@123", user.getPassword());
    }

    @Test
    void testPasswordTooShort() {
        user.setPassword("Short1!");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password must have at least 8 characters")));
    }

    @Test
    void testPasswordMissingRequirements() {
        user.setPassword("password"); // fără majuscule, cifre sau caractere speciale
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password must contain at least one lowercase letter, one uppercase letter, one special character and one digit")));
    }

    // Teste pentru Age
    @Test
    void setAge() {
        user.setAge(20);
        assertEquals(20, user.getAge());
    }

    @Test
    void testAgeTooYoung() {
        user.setAge(10); // sub limita de 12 ani
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("You must be at least 12 years old")));
    }
}
