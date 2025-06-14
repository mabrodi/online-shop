package org.dimchik.service;

import org.dimchik.dao.ProductDao;
import org.dimchik.model.Product;
import org.dimchik.service.validation.ProductValidator;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.dimchik.service.validation.ProductValidator.*;

public class ProductService implements IProductService {
    private final ProductDao productDao;

    public ProductService() {
        this(new ProductDao());
    }

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @Override
    public void addProduct(String name, double price) {
        validateForCreate(name, price);

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCreationDate(LocalDateTime.now());

        productDao.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        validateId(id);

        return productDao.findById(id);
    }

    @Override
    public void updateProduct(long id, String name, double price) {
        validateForUpdate(id, name, price);

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);

        productDao.update(product);
    }

    @Override
    public void deleteProduct(Long id) {
        validateId(id);

        productDao.delete(id);
    }
}
