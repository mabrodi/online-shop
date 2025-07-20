package org.dimchik.dao;

import org.dimchik.entity.Cart;

import java.util.List;

public interface CartDao {
    boolean exists(Cart cart);
    void save(Cart cart);
    void delete(long cartId);
    void clearCart(long userId);
    List<Cart> findCartsByUserId(long userId);
}
