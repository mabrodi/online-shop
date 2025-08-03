package org.dimchik.service;

import org.dimchik.entity.Product;
import org.dimchik.context.Session;

import java.util.List;

public interface CartService {
    void addProduct(Session session, long productId);

    void removeProduct(Session session, long productId);

    void clear(Session session);

    List<Product> getProducts(Session session);

    int size(Session session);
}
