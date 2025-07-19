package org.dimchik.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.dimchik.entity.User;
import org.dimchik.service.AuthService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {
    @Test
    void getCurrentUserShouldReturnUserWhenSessionExists() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        AuthService authService = new AuthServiceImpl();
        User user = new User();
        user.setId(1);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        User result = authService.getCurrentUser(request);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void getCurrentUserShouldReturnNullWhenSessionIsNull() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        AuthService authService = new AuthServiceImpl();
        when(request.getSession(false)).thenReturn(null);

        User result = authService.getCurrentUser(request);

        assertNull(result);
    }

    @Test
    void loginShouldSetUserInSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        User user = new User();

        when(request.getSession()).thenReturn(session);

        AuthService authService = new AuthServiceImpl();
        authService.login(request, user);

        verify(session).setAttribute("user", user);
    }

    @Test
    void isLoggedInShouldReturnTrueWhenUserExists() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        User user = new User();
        AuthService authService = new AuthServiceImpl();

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        assertTrue(authService.isLoggedIn(request));
    }

    @Test
    void isLoggedInShouldReturnFalseWhenNoSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        AuthService authService = new AuthServiceImpl();
        when(request.getSession(false)).thenReturn(null);

        assertFalse(authService.isLoggedIn(request));
    }

    @Test
    void logoutShouldInvalidateSessionWhenExists() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        AuthService authService = new AuthServiceImpl();

        when(request.getSession(false)).thenReturn(session);

        authService.logout(request);

        verify(session).invalidate();
    }

    @Test
    void logoutShouldDoNothingWhenNoSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        AuthService authService = new AuthServiceImpl();
        when(request.getSession(false)).thenReturn(null);

        assertDoesNotThrow(() -> authService.logout(request));
    }
}
