package org.dimchik.dao;

import org.dimchik.entity.Product;

import java.util.List;

public interface ProductDao {
    List<Product> findAll();
    Product findById(Long id);
    List<Product> findBySearch(String search);
    void save(Product product);
    void update(Product product);
    void delete(Long id);
}
