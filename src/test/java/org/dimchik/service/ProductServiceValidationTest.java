package org.dimchik.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductServiceValidationTest {
    @InjectMocks
    ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addProductThrowsIfNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> productService.addProduct(" ", 100.0));
    }

    @Test
    public void addProductThrowsIfPriceIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> productService.addProduct("Phone", -10.0));
    }

    @Test
    public void updateProductThrowsIfIdIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(0, "TV", 99.9));
    }

    @Test
    public void updateProductThrowsIfNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(1, " ", 10.0));
    }
}
