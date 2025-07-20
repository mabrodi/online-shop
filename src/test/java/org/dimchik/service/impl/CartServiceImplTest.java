package org.dimchik.service.impl;

import org.dimchik.dao.CartDao;
import org.dimchik.entity.Cart;
import org.dimchik.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    CartDao cartDao;

    @Test
    void getAllCartsByUserIdShouldReturnCarts() {
        long userId = 1L;
        Cart cart = new Cart();
        cart.setUserId(userId);
        CartService cartService = new CartServiceImpl(cartDao);

        when(cartDao.findCartsByUserId(userId)).thenReturn(List.of(cart));

        List<Cart> result = cartService.getAllCartsByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId());
        verify(cartDao).findCartsByUserId(userId);
    }

    @Test
    void addCartShouldSaveIfNotExists() {
        long userId = 1L;
        long productId = 2L;
        CartService cartService = new CartServiceImpl(cartDao);

        when(cartDao.exists(any(Cart.class))).thenReturn(false);

        cartService.addCart(userId, productId);

        ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);
        verify(cartDao).save(captor.capture());

        Cart saved = captor.getValue();
        assertEquals(userId, saved.getUserId());
        assertEquals(productId, saved.getProductId());
    }

    @Test
    void addCartShouldThrowIfExists() {
        long userId = 1L;
        long productId = 2L;
        CartService cartService = new CartServiceImpl(cartDao);

        when(cartDao.exists(any(Cart.class))).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            cartService.addCart(userId, productId);
        });

        assertEquals("Product already in cart", exception.getMessage());
        verify(cartDao, never()).save(any());
    }

    @Test
    void deleteCartShouldCallDao() {
        long cartId = 99L;
        CartService cartService = new CartServiceImpl(cartDao);

        cartService.deleteCart(cartId);

        verify(cartDao).delete(cartId);
    }

    @Test
    void deleteCartShouldThrowOnInvalidId() {
        long invalidId = -1L;
        CartService cartService = new CartServiceImpl(cartDao);

        assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteCart(invalidId);
        });

        verify(cartDao, never()).delete(anyLong());
    }

    @Test
    void cleanCartByUserIdShouldCallDao() {
        long userId = 123L;
        CartService cartService = new CartServiceImpl(cartDao);

        cartService.cleanCartByUserId(userId);

        verify(cartDao).clearCart(userId);
    }
}