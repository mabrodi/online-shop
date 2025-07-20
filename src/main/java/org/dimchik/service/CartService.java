package org.dimchik.service;

import org.dimchik.entity.Cart;

import java.util.List;

public interface CartService {
    List<Cart> getAllCartsByUserId(long userId);
    void addCart(long userId, long productId);
    void deleteCart(long id);
    void cleanCartByUserId(long userId);
}
