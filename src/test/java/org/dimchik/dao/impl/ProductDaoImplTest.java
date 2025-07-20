package org.dimchik.dao.impl;

import org.dimchik.dao.ProductDao;
import org.dimchik.entity.Product;
import org.dimchik.util.DbUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDaoImplTest {

    @Mock
    DbUtil dbUtil;
    @Mock
    DataSource dataSource;
    @Mock
    Connection connection;
    @Mock
    Statement statement;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    ResultSet resultSet;

    @Test
    void findByIdShouldReturnProductWhenFound() throws Exception {
        when(dbUtil.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("TestProduct");
        when(resultSet.getDouble("price")).thenReturn(100.0);
        when(resultSet.getString("description")).thenReturn("Description");
        when(resultSet.getTimestamp("creation_date")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 12, 0)));

        ProductDao dao = new ProductDaoImpl(dbUtil);
        Product product = dao.findById(1L);

        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("TestProduct", product.getName());
        assertEquals(100.0, product.getPrice());
        assertEquals("Description", product.getDescription());
        assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0), product.getCreationDate());

        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void findByIdShouldReturnNullWhenNotFound() throws Exception {
        when(dbUtil.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        ProductDao dao = new ProductDaoImpl(dbUtil);
        Product product = dao.findById(99L);

        assertNull(product);
    }

    @Test
    void findAllShouldReturnProductList() throws Exception {
        when(dbUtil.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getLong("id")).thenReturn(1L, 2L);
        when(resultSet.getString("name")).thenReturn("Product1", "Product2");
        when(resultSet.getDouble("price")).thenReturn(10.0, 20.0);
        when(resultSet.getString("description")).thenReturn("Desc1", "Desc2");
        when(resultSet.getTimestamp("creation_date")).thenReturn(
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 12, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 2, 13, 0))
        );

        ProductDao dao = new ProductDaoImpl(dbUtil);
        List<Product> products = dao.findAll();

        assertEquals(2, products.size());

        assertEquals("Product1", products.get(0).getName());
        assertEquals("Product2", products.get(1).getName());
    }
}