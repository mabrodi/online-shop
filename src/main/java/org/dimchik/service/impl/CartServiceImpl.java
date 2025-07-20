package org.dimchik.service.impl;

import org.dimchik.dao.CartDao;
import org.dimchik.entity.Cart;
import org.dimchik.service.CartService;

import java.util.List;
import static org.dimchik.service.validation.CartValidator.*;

public class CartServiceImpl implements CartService {
    private final CartDao cartDao;

    public CartServiceImpl(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    @Override
    public List<Cart> getAllCartsByUserId(long userId) {
        return cartDao.findCartsByUserId(userId);
    }

    @Override
    public void addCart(long userId, long productId) {
        validateAdd(productId);

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setProductId(productId);

        if (cartDao.exists(cart)) {
            throw new IllegalStateException("Product already in cart");
        }

        cartDao.save(cart);
    }

    @Override
    public void deleteCart(long id) {
        validateCartId(id);

        cartDao.delete(id);
    }

    @Override
    public void cleanCartByUserId(long userId) {
        cartDao.clearCart(userId);
    }
}
