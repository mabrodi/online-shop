package org.dimchik.dao;

import org.dimchik.model.Product;
import org.dimchik.util.DbUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductDaoTest {
    @Mock DbUtil dbUtil;
    @Mock Connection connection;
    @Mock PreparedStatement preparedStatement;
    @Mock Statement statement;
    @Mock ResultSet resultSet;

    ProductDao productDao;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        productDao = new ProductDao(dbUtil);
    }

    @Test
    void findAllReturnsProducts() throws Exception {
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false); // 1 продукт, потом конец
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("Product1");
        when(resultSet.getDouble("price")).thenReturn(123.45);
        when(resultSet.getTimestamp("creation_date"))
                .thenReturn(Timestamp.valueOf(LocalDateTime.of(2024, 5, 20, 10, 15)));

        List<Product> products = productDao.findAll();

        assertEquals(1, products.size());
        Product p = products.getFirst();
        assertEquals(1L, p.getId());
        assertEquals("Product1", p.getName());
        assertEquals(123.45, p.getPrice());
        assertEquals(LocalDateTime.of(2024, 5, 20, 10, 15), p.getCreationDate());

        verify(connection).close();
    }

    @Test
    void findByIdReturnsProduct() throws Exception {
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(2L);
        when(resultSet.getString("name")).thenReturn("Product2");
        when(resultSet.getDouble("price")).thenReturn(200.0);
        when(resultSet.getTimestamp("creation_date"))
                .thenReturn(Timestamp.valueOf(LocalDateTime.of(2024, 5, 21, 12, 30)));

        Product found = productDao.findById(2L);

        assertNotNull(found);
        assertEquals(2L, found.getId());
        assertEquals("Product2", found.getName());
        assertEquals(200.0, found.getPrice());
        assertEquals(LocalDateTime.of(2024, 5, 21, 12, 30), found.getCreationDate());
    }

    @Test
    void saveInsertsProduct() throws Exception {
        Product product = new Product();
        product.setName("SavedProduct");
        product.setPrice(123.0);
        product.setCreationDate(LocalDateTime.of(2024, 5, 22, 13, 0));

        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(99L);

        productDao.save(product);

        verify(preparedStatement).setString(1, "SavedProduct");
        verify(preparedStatement).setDouble(2, 123.0);
        verify(preparedStatement).setTimestamp(eq(3), any(Timestamp.class));
        verify(preparedStatement).executeUpdate();
        verify(resultSet).next();
        verify(resultSet).getLong(1);
        assertEquals(99L, product.getId());
    }

    @Test
    void updateProduct() throws Exception {
        Product product = new Product();
        product.setId(10L);
        product.setName("UpdatedProduct");
        product.setPrice(777.0);

        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        productDao.update(product);

        verify(preparedStatement).setString(1, "UpdatedProduct");
        verify(preparedStatement).setDouble(2, 777.0);
        verify(preparedStatement).setLong(3, 10L);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void deleteProduct() throws Exception {
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        productDao.delete(33L);

        verify(preparedStatement).setLong(1, 33L);
        verify(preparedStatement).executeUpdate();
    }
}


