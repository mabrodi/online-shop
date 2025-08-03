package org.dimchik.dao.mapper;

import org.dimchik.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRowMapperTest {

    @Mock
    ResultSet resultSet;

    @Test
    public void mapRowShouldReturnProduct() throws SQLException {
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("Product");
        when(resultSet.getDouble("price")).thenReturn(10.50);
        when(resultSet.getString("description")).thenReturn("Description");
        when(resultSet.getTimestamp("creation_date")).thenReturn(
                Timestamp.valueOf(LocalDateTime.of(2025, 7, 10, 12, 0))
        );

        Product product = ProductRowMapper.mapRow(resultSet);
        assertEquals(1L, product.getId());
        assertEquals("Product", product.getName());
        assertEquals(10.50, product.getPrice());
        assertEquals("Description", product.getDescription());
    }
}