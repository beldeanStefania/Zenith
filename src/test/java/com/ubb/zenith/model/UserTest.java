package com.ubb.zenith.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Validator validator;

    @BeforeEach
    void setUp() {
        user = new User();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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

    @Test
    void testUsernameValidation() {
        user.setUsername("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        user.setUsername("Jo");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        user.setUsername("ValidUser");
        violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void setEmail() {
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testEmailValidation() {
        user.setEmail("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        user.setEmail("valid@example.com");
        violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void setPassword() {
        user.setPassword("Valid123!");
        assertEquals("Valid123!", user.getPassword());
    }

    @Test
    void testPasswordValidation() {
        user.setPassword("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        user.setPassword("123");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        user.setPassword("Valid123!");
        violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void setAge() {
        user.setAge(25);
        assertEquals(25, user.getAge());
    }

    @Test
    void testAgeValidation() {
        user.setAge(10);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        user.setAge(20);
        violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }
}