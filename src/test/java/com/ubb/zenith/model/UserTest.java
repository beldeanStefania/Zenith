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
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        user = new User();
    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    @Test
    void testIdSetterAndGetter() {
        user.setId(1);
        assertEquals(1, user.getId());
    }

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
    void setEmail() {
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void setPassword() {
        user.setPassword("Valid123!");
        assertEquals("Valid123!", user.getPassword());
    }

    @Test
    void setAge() {
        user.setAge(25);
        assertEquals(25, user.getAge());
    }
    @Test
    void testUsernameValidation() {
        user.setUsername("");
        user.setEmail("valid@example.com");
        user.setPassword("Valid123!");
        user.setAge(20);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertFalse(violations.isEmpty());

        // Test short username
        user.setUsername("Jo");
        violations = validator.validate(user);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertFalse(violations.isEmpty());

        // Test valid username
        user.setUsername("ValidUsername");
        violations = validator.validate(user);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertTrue(violations.isEmpty());
    }

//    @Test
//    void testPasswordValidation() {
//        user.setPassword("");
//        Set<ConstraintViolation<User>> violations = validator.validate(user);
//        assertFalse(violations.isEmpty());
//
//        user.setPassword("123");
//        violations = validator.validate(user);
//        assertFalse(violations.isEmpty());
//
//        user.setPassword("Valid123!");
//        violations = validator.validate(user);
//        assertTrue(violations.isEmpty());
//    }

    @Test
    void testEmailValidation() {
        // Test empty email
        user.setUsername("ValidUsername");
        user.setEmail("");
        user.setPassword("Valid123!");
        user.setAge(20);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertFalse(violations.isEmpty());

        // Test invalid email format
        user.setEmail("invalid-email");
        violations = validator.validate(user);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertFalse(violations.isEmpty());

        // Test valid email
        user.setEmail("valid@example.com");
        violations = validator.validate(user);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertTrue(violations.isEmpty());
    }

    @Test
    void testPasswordValidation() {
        // Test empty password
        user.setUsername("ValidUsername");
        user.setEmail("valid@example.com");
        user.setPassword("");
        user.setAge(20);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertFalse(violations.isEmpty());

        // Test short password
        user.setPassword("123");
        violations = validator.validate(user);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertFalse(violations.isEmpty());

        // Test password without required characters
        user.setPassword("password");
        violations = validator.validate(user);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertFalse(violations.isEmpty());

        // Test valid password
        user.setPassword("Valid123!");
        violations = validator.validate(user);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertTrue(violations.isEmpty());
    }

    @Test
    void testAgeValidation() {
        // Test age below minimum
        user.setUsername("ValidUsername");
        user.setEmail("valid@example.com");
        user.setPassword("Valid123!");
        user.setAge(10);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertFalse(violations.isEmpty());

        // Test valid age
        user.setAge(20);
        violations = validator.validate(user);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
        assertTrue(violations.isEmpty());
    }


//    @Test
//    void testAgeValidation() {
//        user.setAge(10);
//        Set<ConstraintViolation<User>> violations = validator.validate(user);
//        assertFalse(violations.isEmpty());
//
//        user.setAge(20);
//        violations = validator.validate(user);
//        assertTrue(violations.isEmpty());
//    }
}