package org.dimchik.service.impl;

import org.dimchik.dao.UserDao;
import org.dimchik.entity.User;
import org.dimchik.util.CryptoHash;
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
    void loginShouldReturnUserWhenCredentialAndValid() {
        String email = "john@example.com";
        String rawPassword = "password123";
        String hashedPassword = CryptoHash.hash(rawPassword);
        UserServiceImpl service = new UserServiceImpl(userDao);

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setId(1L);
        user.setName("John Doe");

        when(userDao.findByEmail(email)).thenReturn(user);

        User result = service.login(email, rawPassword);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getName(), result.getName());
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

        when(userDao.findByEmail("user@example.com")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.login("user@example.com", "password")
        );

        assertEquals("Invalid email or password", ex.getMessage());
        verify(userDao).findByEmail("user@example.com");
    }
}