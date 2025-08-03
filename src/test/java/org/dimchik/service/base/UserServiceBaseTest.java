package org.dimchik.service.base;

import org.dimchik.crypto.CryptoHash;
import org.dimchik.dao.UserDao;
import org.dimchik.entity.User;
import org.dimchik.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceBaseTest {
    @Mock
     UserDao userDao;

    @Test
    void findAndValidateShouldReturnUserWhenCredentialAndValid() {
        String email = "john@example.com";
        String rawPassword = "password123";
        String hashedPassword = CryptoHash.hash(rawPassword);
        UserService service = new UserServiceBase(userDao);

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setId(1L);
        user.setName("John Doe");

        when(userDao.findByEmail(email)).thenReturn(user);

        User result = service.authenticate(email, rawPassword);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getName(), result.getName());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        String email = "notfound@example.com";
        when(userDao.findByEmail(email)).thenReturn(null);

        UserService service = new UserServiceBase(userDao);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.authenticate(email, "anyPassword")
        );

        assertEquals("Invalid email or password", exception.getMessage());
        verify(userDao).findByEmail(email);
    }

    @Test
    void shouldThrowWhenPasswordIsIncorrect() {
        String email = "john@example.com";
        String correctPassword = "correct";
        String incorrectPassword = "wrong";

        User user = new User();
        user.setEmail(email);
        user.setPassword(CryptoHash.hash(correctPassword));

        when(userDao.findByEmail(email)).thenReturn(user);

        UserService service = new UserServiceBase(userDao);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.authenticate(email, incorrectPassword)
        );

        assertEquals("Invalid email or password", exception.getMessage());
    }
}