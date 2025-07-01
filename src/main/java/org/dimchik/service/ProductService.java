package org.dimchik.service;

import org.dimchik.dao.IProductDao;
import org.dimchik.dao.ProductDao;
import org.dimchik.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

import static org.dimchik.service.validation.ProductValidator.*;

public class ProductService implements IProductService {
    private final IProductDao productDao;

    public ProductService(IProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @Override
    public List<Product> searchProducts(String query) {
        if (query == null || query.isBlank()) {
            return getAllProducts();
        }

        return productDao.findBySearch(query);
    }

    @Override
    public void addProduct(String name, double price, String description) {
        validateForCreate(name, price);

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCreationDate(LocalDateTime.now());
        product.setDescription(description);

        productDao.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        validateId(id);

        return productDao.findById(id);
    }

    @Override
    public void updateProduct(long id, String name, double price, String description) {
        validateForUpdate(id, name, price);

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);

        productDao.update(product);
    }

    @Override
    public void deleteProduct(Long id) {
        validateId(id);

        productDao.delete(id);
    }
}
