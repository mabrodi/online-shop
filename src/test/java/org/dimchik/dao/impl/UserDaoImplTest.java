package org.dimchik.dao.impl;

import org.dimchik.dao.UserDao;
import org.dimchik.entity.User;
import org.dimchik.util.DbUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDaoImplTest {

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
    void findByEmailShouldReturnUserWhenFound() throws Exception {
        when(dbUtil.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getString("password")).thenReturn("secret");

        UserDao dao = new UserDaoImpl(dbUtil);

        User user = dao.findByEmail("john@example.com");

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("secret", user.getPassword());

        verify(preparedStatement).setString(1, "john@example.com");
        verify(preparedStatement).executeQuery();
    }

    @Test
    void findByEmailShouldReturnNullWhenNotFound() throws Exception {
        when(dbUtil.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        UserDao dao = new UserDaoImpl(dbUtil);

        User user = dao.findByEmail("john@example.com");

        assertNull(user);
    }
}