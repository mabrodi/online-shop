package org.dimchik.service.impl;

import org.dimchik.dao.UserDao;
import org.dimchik.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserDao userDao;

    @Test
    void loginShouldReturnUserWhenValid() {
        UserServiceImpl service = new UserServiceImpl(userDao);

        User user = new User();
        when(userDao.findByEmailAndPassword("user@example.com", "password")).thenReturn(user);

        User result = service.login("user@example.com", "password");

        assertEquals(user, result);
        verify(userDao).findByEmailAndPassword("user@example.com", "password");
    }

    @Test
    void loginShouldThrowWhenValidationFails_nullEmail() {
        UserServiceImpl service = new UserServiceImpl(userDao);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.login(null, "password")
        );

        assertEquals("Email must not be empty", ex.getMessage());
        verifyNoInteractions(userDao);
    }

    @Test
    void loginShouldThrowWhenValidationFails_invalidEmail() {
        UserServiceImpl service = new UserServiceImpl(userDao);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.login("invalid-email", "password")
        );

        assertEquals("Invalid email format", ex.getMessage());
        verifyNoInteractions(userDao);
    }

    @Test
    void loginShouldThrowWhenUserNotFound() {
        UserServiceImpl service = new UserServiceImpl(userDao);

        when(userDao.findByEmailAndPassword("user@example.com", "password")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.login("user@example.com", "password")
        );

        assertEquals("Invalid email or password", ex.getMessage());
        verify(userDao).findByEmailAndPassword("user@example.com", "password");
    }
}