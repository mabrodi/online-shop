package org.dimchik.service.validation;

public class ProductValidator {
    public static void validateForCreate(String name, double price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Product price must be non-negative");
        }
    }

    public static void validateForUpdate(long id, String name, double price) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        validateForCreate(name, price);
    }

    public static void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
    }
}
