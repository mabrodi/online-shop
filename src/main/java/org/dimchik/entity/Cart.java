package org.dimchik.entity;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<Product> products = new ArrayList<>();

    public void add(Product product) {
        if (!contains(product)) {
            products.add(product);
        }
    }

    public void remove(long productId) {
        for (Product product : products) {
            if (product.getId() == productId) {
                products.remove(product);
                break;
            }
        }
    }

    public void clear() {
        products.clear();
    }

    public boolean contains(Product product) {
        for (Product p : products) {
            if (p.getId() == product.getId()) {
                return true;
            }
        }

        return false;
    }

    public List<Product> getProducts() {
        return products;
    }

    public int size() {
        return products.size();
    }
}
