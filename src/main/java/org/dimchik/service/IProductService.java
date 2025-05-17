package org.dimchik.service;

import org.dimchik.model.Product;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();
    void addProduct(String name, double price);
    Product getProductById(Long id);
    void updateProduct(long id, String name, double price);
    void deleteProduct(Long id);
}
