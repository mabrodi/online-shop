package org.dimchik.service.base;

import org.dimchik.context.Session;
import org.dimchik.entity.User;
import org.dimchik.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityServiceBaseTest {

    private UserService userService;
    private SecurityServiceBase securityService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        securityService = new SecurityServiceBase(userService, 10);
    }

    @Test
    void loginShouldReturnTokenAndStoreSession() {
        String email = "john@example.com";
        String password = "securePassword";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        when(userService.authenticate(email, password)).thenReturn(user);

        String token = securityService.login(email, password);

        assertNotNull(token);
        Session session = securityService.getCurrentSession(token);
        assertNotNull(session);
        assertEquals(user, session.getUser());
    }

    @Test
    void logoutShouldRemoveSession() {
        String email = "john@example.com";
        String password = "securePassword";
        User user = new User();
        user.setEmail(email);

        when(userService.authenticate(email, password)).thenReturn(user);
        String token = securityService.login(email, password);

        assertNotNull(securityService.getCurrentSession(token));
        securityService.logout(token);
        assertNull(securityService.getCurrentSession(token));
    }

    @Test
    void isLoggableShouldReturnTrueIfSessionIsValid() {
        User user = new User();
        user.setEmail("valid@example.com");

        when(userService.authenticate(anyString(), anyString())).thenReturn(user);

        String token = securityService.login("valid@example.com", "password");

        assertTrue(securityService.isLoggable(token));
    }

    @Test
    void isLoggableShouldReturnFalseIfTokenDoesNotExist() {
        assertFalse(securityService.isLoggable("non-existent-token"));
    }

    @Test
    void getCurrentSessionShouldReturnNullIfTokenIsUnknown() {
        assertNull(securityService.getCurrentSession("unknown"));
    }
}
