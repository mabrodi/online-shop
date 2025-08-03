package org.dimchik.web.servlet.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dimchik.service.SecurityService;
import org.dimchik.service.base.SecurityServiceBase;
import org.dimchik.web.session.SessionCookieHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutServletTest {

    private LogoutServlet servlet;

    @Mock
    private SecurityService securityService;
    @Mock
    private SessionCookieHandler sessionCookieHandler;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        servlet = new LogoutServlet(securityService, sessionCookieHandler);
    }

    @Test
    void doGetShouldLogoutAndClearCookieAndRedirect() throws Exception {
        when(sessionCookieHandler.extractId(request)).thenReturn("TOKEN_123");

        servlet.doGet(request, response);

        verify(securityService).logout("TOKEN_123");
        verify(sessionCookieHandler).clear(response);
        verify(response).sendRedirect("/login");
    }
}