package org.dimchik.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class SessionCookieHandlerTest {

    @Test
    void extractIdShouldReturnSessionIdIfCookiePresent() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        SessionCookieHandler sessionCookieHandler = new SessionCookieHandler("session", 100);

        Cookie expected = new Cookie("session", "abc123");
        when(request.getCookies()).thenReturn(new Cookie[]{
                new Cookie("other", "123"),
                expected
        });

        String actual = sessionCookieHandler.extractId(request);
        assertEquals("abc123", actual);
    }

    @Test
    void extractIdShouldReturnNullIfNoCookies() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        SessionCookieHandler sessionCookieHandler = new SessionCookieHandler("session", 100);
        String actual = sessionCookieHandler.extractId(request);
        assertNull(actual);
    }

    @Test
    void extractIdShouldReturnNullIfNoMatchingCookie() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{
                new Cookie("wrongName", "value")
        });

        SessionCookieHandler sessionCookieHandler = new SessionCookieHandler("session", 100);
        String actual = sessionCookieHandler.extractId(request);
        assertNull(actual);
    }

    @Test
    void setShouldAddCorrectCookie() {
        HttpServletResponse response = mock(HttpServletResponse.class);
        SessionCookieHandler sessionCookieHandler = new SessionCookieHandler("session", 100);
        sessionCookieHandler.set(response, "token123");

        verify(response).addCookie(argThat(cookie ->
                cookie.getName().equals("session") &&
                        cookie.getValue().equals("token123") &&
                        cookie.isHttpOnly() &&
                        cookie.getMaxAge() == 100
        ));
    }

    @Test
    void clearShouldAddCookieWithMaxAgeZero() {
        HttpServletResponse response = mock(HttpServletResponse.class);
        SessionCookieHandler sessionCookieHandler = new SessionCookieHandler("session", 100);
        sessionCookieHandler.clear(response);

        verify(response).addCookie(argThat(cookie ->
                cookie.getName().equals("session") &&
                        cookie.getMaxAge() == 0
        ));
    }
}