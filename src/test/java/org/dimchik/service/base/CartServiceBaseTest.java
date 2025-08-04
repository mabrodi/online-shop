package org.dimchik.service.base;

import org.dimchik.context.Session;
import org.dimchik.entity.Product;
import org.dimchik.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceBaseTest {
    @Mock
    private ProductService productService;

    private CartServiceBase cartService;

    private Session session;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceBase(productService);
        session = new Session(null, 3600);
    }

    @Test
    void addProductShouldAddProductToCartWhenProductExists() {
        Product product = new Product();
        product.setId(1L);
        when(productService.getProductById(1L)).thenReturn(product);

        cartService.addProduct(session, 1L);

        List<Product> products = session.getCart().getProducts();
        assertEquals(1, products.size());
        assertEquals(product, products.getFirst());
    }

    @Test
    void addProductShouldNotAddProductWhenProductNotFound() {
        when(productService.getProductById(999L)).thenReturn(null);
        cartService.addProduct(session, 999L);
        assertTrue(session.getCart().getProducts().isEmpty());
    }

    @Test
    void removeProductShouldRemoveProductById() {
        Product product = new Product();
        product.setId(1L);
        when(productService.getProductById(1L)).thenReturn(product);

        cartService.addProduct(session, 1L);
        assertEquals(1, session.getCart().getProducts().size());

        cartService.removeProduct(session, 1L);
        assertTrue(session.getCart().getProducts().isEmpty());
    }

    @Test
    void clearShouldRemoveAllProducts() {
        Product product1 = new Product(); product1.setId(1L);
        Product product2 = new Product(); product2.setId(2L);
        when(productService.getProductById(1L)).thenReturn(product1);
        when(productService.getProductById(2L)).thenReturn(product2);

        cartService.addProduct(session, 1L);
        cartService.addProduct(session, 2L);
        assertEquals(2, session.getCart().size());

        cartService.clear(session);
        assertEquals(0, session.getCart().size());
    }

    @Test
    void getProductsShouldReturnAllProducts() {
        Product product = new Product();
        product.setId(1L);
        when(productService.getProductById(1L)).thenReturn(product);

        cartService.addProduct(session, 1L);

        List<Product> products = cartService.getProducts(session);
        assertEquals(1, products.size());
        assertEquals(product, products.getFirst());
    }

    @Test
    void sizeShouldReturnNumberOfProductsInCart() {
        Product product = new Product();
        product.setId(1L);
        when(productService.getProductById(1L)).thenReturn(product);

        cartService.addProduct(session, 1L);
        assertEquals(1, cartService.size(session));
    }
}