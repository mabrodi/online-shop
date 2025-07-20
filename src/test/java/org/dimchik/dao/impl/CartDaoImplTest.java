package org.dimchik.dao.impl;

import org.dimchik.dao.CartDao;
import org.dimchik.entity.Cart;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartDaoImplTest {
    @Mock
    DbUtil dbUtil;
    @Mock
    DataSource dataSource;
    @Mock
    Connection connection;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    ResultSet resultSet;

    @Test
    void findAllShouldReturnProductList() throws Exception {
        when(dbUtil.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getLong("cart_id")).thenReturn(1L, 2L);
        when(resultSet.getLong("user_id")).thenReturn(5L, 5L);
        when(resultSet.getLong("product_id")).thenReturn(10L, 20L);

        when(resultSet.getString("name")).thenReturn("Product1", "Product2");
        when(resultSet.getDouble("price")).thenReturn(10.0, 20.0);
        when(resultSet.getString("description")).thenReturn("Desc1", "Desc2");
        when(resultSet.getTimestamp("product_creation_date")).thenReturn(
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 1, 12, 0)),
                Timestamp.valueOf(LocalDateTime.of(2023, 1, 2, 13, 0))
        );

        CartDao dao = new CartDaoImpl(dbUtil);
        List<Cart> carts = dao.findCartsByUserId(5L);

        assertNotNull(carts);
        assertEquals(2, carts.size());
        assertEquals(1L, carts.get(0).getId());
        assertEquals("Product1", carts.get(0).getProduct().getName());
        assertEquals("Product2", carts.get(1).getProduct().getName());

        verify(preparedStatement).setLong(1, 5L);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void existsShouldReturnTrueIfRecordExists() throws Exception {
        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setProductId(2L);

        when(dbUtil.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        CartDao dao = new CartDaoImpl(dbUtil);
        boolean result = dao.exists(cart);

        assertTrue(result);
    }

    @Test
    void saveShouldInsertCart() throws Exception {
        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setProductId(2L);

        when(dbUtil.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        CartDao dao = new CartDaoImpl(dbUtil);
        dao.save(cart);

        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).setLong(2, 2L);
        verify(preparedStatement).executeUpdate();
    }
}