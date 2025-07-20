package org.dimchik.service.validation;

public class CartValidator {
    public static void validateAdd(long productId) {
        if (!isValidId(productId)) {
            throw new IllegalArgumentException("Invalid product id");
        }
    }

    public static void validateCartId(Long cartId) {
        if (!isValidId(cartId)) {
            throw new IllegalArgumentException("Invalid cart id");
        }
    }

    private static boolean isValidId(Long id) {
        return id != null && id > 0;
    }
}
