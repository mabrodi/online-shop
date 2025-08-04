package org.dimchik.service.base;

import org.dimchik.dao.ProductDao;
import org.dimchik.entity.Product;
import org.dimchik.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceBaseTest {

    @Mock
    ProductDao productDao;

    @Test
    void getAllProductsShouldReturnProducts() {
        ProductService service = new ProductServiceBase(productDao);
        List<Product> products = List.of(new Product());
        when(productDao.findAll()).thenReturn(products);

        List<Product> result = service.getAllProducts();

        assertEquals(products, result);
        verify(productDao).findAll();
    }

    @Test
    void searchProductsShouldCallFindBySearchWhenQueryNotBlank() {
        ProductService service = new ProductServiceBase(productDao);
        List<Product> products = List.of(new Product());
        when(productDao.findBySearch("apple")).thenReturn(products);

        List<Product> result = service.searchProducts("apple");

        assertEquals(products, result);
        verify(productDao).findBySearch("apple");
    }

    @Test
    void searchProductsShouldCallFindAllWhenQueryBlank() {
        ProductService service = new ProductServiceBase(productDao);
        List<Product> products = List.of(new Product());
        when(productDao.findAll()).thenReturn(products);

        List<Product> result = service.searchProducts("  ");

        assertEquals(products, result);
        verify(productDao).findAll();
    }

    @Test
    void addProductShouldValidateAndSave() {
        ProductService service = new ProductServiceBase(productDao);

        service.addProduct("Product", 10.0, "Description");

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productDao).save(captor.capture());

        Product saved = captor.getValue();
        assertEquals("Product", saved.getName());
        assertEquals(10.0, saved.getPrice());
        assertEquals("Description", saved.getDescription());
        assertNotNull(saved.getCreationDate());
    }

    @Test
    void getProductByIdShouldValidateAndReturn() {
        ProductService service = new ProductServiceBase(productDao);
        Product product = new Product();
        when(productDao.findById(1L)).thenReturn(product);

        Product result = service.getProductById(1L);

        assertEquals(product, result);
        verify(productDao).findById(1L);
    }


    @Test
    void updateProductShouldValidateAndUpdate() {
        ProductService service = new ProductServiceBase(productDao);

        service.updateProduct(1L, "Updated", 20.0, "Updated desc");

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productDao).update(captor.capture());

        Product updated = captor.getValue();
        assertEquals(1L, updated.getId());
        assertEquals("Updated", updated.getName());
        assertEquals(20.0, updated.getPrice());
        assertEquals("Updated desc", updated.getDescription());
    }


    @Test
    void deleteProductShouldValidateAndDelete() {
        ProductService service = new ProductServiceBase(productDao);
        service.deleteProduct(1L);
        verify(productDao).delete(1L);
    }
}