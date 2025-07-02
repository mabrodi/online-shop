package org.dimchik.service.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    @Test
    void validateShouldPassWithValidInput() {
        assertDoesNotThrow(() -> UserValidator.validate("user@example.com", "password123"));
    }

    @Test
    void validateShouldThrowWhenEmailIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> UserValidator.validate(null, "password123")
        );
        assertEquals("Email must not be empty", ex.getMessage());
    }

    @Test
    void validateShouldThrowWhenEmailIsBlank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> UserValidator.validate("   ", "password123")
        );
        assertEquals("Email must not be empty", ex.getMessage());
    }

    @Test
    void validateShouldThrowWhenEmailInvalidFormat() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> UserValidator.validate("invalid-email", "password123")
        );
        assertEquals("Invalid email format", ex.getMessage());
    }

    @Test
    void validateShouldThrowWhenPasswordIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> UserValidator.validate("user@example.com", null)
        );
        assertEquals("Password must not be empty", ex.getMessage());
    }

    @Test
    void validateShouldThrowWhenPasswordIsBlank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> UserValidator.validate("user@example.com", "   ")
        );
        assertEquals("Password must not be empty", ex.getMessage());
    }

    @Test
    void validateShouldThrowWhenPasswordTooShort() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> UserValidator.validate("user@example.com", "12")
        );
        assertEquals("Password must be at least 3 characters", ex.getMessage());
    }
}