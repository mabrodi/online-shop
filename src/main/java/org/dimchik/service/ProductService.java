package org.dimchik.service;

import org.dimchik.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    List<Product> searchProducts(String query);

    void addProduct(String name, double price, String description);

    Product getProductById(Long id);

    void updateProduct(long id, String name, double price, String description);

    void deleteProduct(Long id);
}
