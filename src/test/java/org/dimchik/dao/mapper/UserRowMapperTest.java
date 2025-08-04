package org.dimchik.dao.mapper;

import org.dimchik.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRowMapperTest {

    @Mock
    ResultSet resultSet;

    @Test
    public void mapRowShouldReturnUser() throws SQLException {
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("Admin");
        when(resultSet.getString("email")).thenReturn("test@example.com");
        when(resultSet.getString("password")).thenReturn("password");

        User user = UserRowMapper.mapRow(resultSet);
        assertEquals(1L, user.getId());
        assertEquals("Admin", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
    }

}