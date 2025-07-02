package org.dimchik.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.dimchik.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionUtilTest {

    @Test
    void getCurrentUserShouldReturnUserWhenSessionExists() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        User user = new User();
        user.setId(1);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        User result = SessionUtil.getCurrentUser(request);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void getCurrentUserShouldReturnNullWhenSessionIsNull() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession(false)).thenReturn(null);

        User result = SessionUtil.getCurrentUser(request);

        assertNull(result);
    }

    @Test
    void loginShouldSetUserInSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        User user = new User();

        when(request.getSession()).thenReturn(session);

        SessionUtil.login(request, user);

        verify(session).setAttribute("user", user);
    }

    @Test
    void isLoggedInShouldReturnTrueWhenUserExists() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        User user = new User();

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        assertTrue(SessionUtil.isLoggedIn(request));
    }

    @Test
    void isLoggedInShouldReturnFalseWhenNoSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession(false)).thenReturn(null);

        assertFalse(SessionUtil.isLoggedIn(request));
    }

    @Test
    void logoutShouldInvalidateSessionWhenExists() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);

        SessionUtil.logout(request);

        verify(session).invalidate();
    }

    @Test
    void logoutShouldDoNothingWhenNoSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession(false)).thenReturn(null);

        assertDoesNotThrow(() -> SessionUtil.logout(request));
    }
}