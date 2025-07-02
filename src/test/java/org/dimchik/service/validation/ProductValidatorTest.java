package org.dimchik.service.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductValidatorTest {
    @Test
    void validateForCreateShouldPassWithValidInput() {
        assertDoesNotThrow(() -> ProductValidator.validateForCreate("Product", 10.0));
    }

    @Test
    void validateForCreateShouldThrowWhenNameNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ProductValidator.validateForCreate(null, 10.0)
        );
        assertEquals("Product name must not be null or empty", ex.getMessage());
    }

    @Test
    void validateForCreateShouldThrowWhenNameBlank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ProductValidator.validateForCreate("  ", 10.0)
        );
        assertEquals("Product name must not be null or empty", ex.getMessage());
    }

    @Test
    void validateForCreateShouldThrowWhenPriceNegative() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ProductValidator.validateForCreate("Product", -5.0)
        );
        assertEquals("Product price must be non-negative", ex.getMessage());
    }

    @Test
    void validateForUpdateShouldPassWithValidInput() {
        assertDoesNotThrow(() -> ProductValidator.validateForUpdate(1L, "Product", 20.0));
    }

    @Test
    void validateForUpdateShouldThrowWhenIdInvalid() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ProductValidator.validateForUpdate(0L, "Product", 20.0)
        );
        assertEquals("Invalid product ID", ex.getMessage());
    }

    @Test
    void validateForUpdateShouldThrowWhenNameInvalid() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ProductValidator.validateForUpdate(1L, "", 20.0)
        );
        assertEquals("Product name must not be null or empty", ex.getMessage());
    }

    @Test
    void validateForUpdateShouldThrowWhenPriceInvalid() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ProductValidator.validateForUpdate(1L, "Product", -1.0)
        );
        assertEquals("Product price must be non-negative", ex.getMessage());
    }

    @Test
    void validateIdShouldPassWithValidId() {
        assertDoesNotThrow(() -> ProductValidator.validateId(1L));
    }

    @Test
    void validateIdShouldThrowWhenNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ProductValidator.validateId(null)
        );
        assertEquals("Invalid product ID", ex.getMessage());
    }

    @Test
    void validateIdShouldThrowWhenZero() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ProductValidator.validateId(0L)
        );
        assertEquals("Invalid product ID", ex.getMessage());
    }

    @Test
    void validateIdShouldThrowWhenNegative() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ProductValidator.validateId(-5L)
        );
        assertEquals("Invalid product ID", ex.getMessage());
    }
}