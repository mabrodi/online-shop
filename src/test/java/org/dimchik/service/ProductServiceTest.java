package org.dimchik.service;

import org.dimchik.dao.ProductDao;
import org.dimchik.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
    @Mock ProductDao productDao;
    @InjectMocks ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllProductsReturnsProductsFromDao() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setCreationDate(LocalDateTime.now());
        when(productDao.findAll()).thenReturn(List.of(product));

        List<Product> products = productService.getAllProducts();

        assertEquals(1, products.size());
        assertEquals("Test Product", products.getFirst().getName());
        verify(productDao).findAll();
    }

    @Test
    public void addProductPassesCorrectProductToDao() {
        productService.addProduct("Phone", 500.0);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productDao).save(captor.capture());
        Product saved = captor.getValue();
        assertEquals("Phone", saved.getName());
        assertEquals(500.0, saved.getPrice());
        assertNotNull(saved.getCreationDate());
    }

    @Test
    public void getProductByIdReturnsProduct() {
        Product product = new Product();
        product.setId(2L);
        product.setName("Laptop");
        when(productDao.findById(2L)).thenReturn(product);

        Product found = productService.getProductById(2L);
        assertEquals("Laptop", found.getName());
        verify(productDao).findById(2L);
    }

    @Test
    public void updateProductCallsDaoUpdate() {
        productService.updateProduct(3L, "Monitor", 200.0);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productDao).update(captor.capture());
        Product updated = captor.getValue();
        assertEquals(3L, updated.getId());
        assertEquals("Monitor", updated.getName());
        assertEquals(200.0, updated.getPrice());
    }

    @Test
    public void deleteProductCallsDaoDelete() {
        productService.deleteProduct(4L);
        verify(productDao).delete(4L);
    }
}