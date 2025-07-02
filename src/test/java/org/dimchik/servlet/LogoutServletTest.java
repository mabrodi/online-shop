package org.dimchik.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutServletTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Test
    void doGetShouldInvalidateSessionAndRedirect() throws Exception {
        LogoutServlet servlet = new LogoutServlet();

        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);

        servlet.doGet(request, response);

        verify(session).invalidate();
        verify(response).sendRedirect("/login");
    }

    @Test
    void doGetShouldRedirectWhenNoSession() throws Exception {
        LogoutServlet servlet = new LogoutServlet();

        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/login");
    }
}