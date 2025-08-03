package org.dimchik.service.base;

import org.dimchik.entity.Product;
import org.dimchik.service.CartService;
import org.dimchik.service.ProductService;
import org.dimchik.context.Session;

import java.util.List;

public class CartServiceBase implements CartService {
    private final ProductService productService;

    public CartServiceBase(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void addProduct(Session session, long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            session.getCart().add(product);
        }
    }

    @Override
    public void removeProduct(Session session, long productId) {
        session.getCart().remove(productId);
    }

    @Override
    public void clear(Session session) {
        session.getCart().clear();
    }

    @Override
    public List<Product> getProducts(Session session) {
        return session.getCart().getProducts();
    }

    @Override
    public int size(Session session) {
        return session.getCart().size();
    }
}
