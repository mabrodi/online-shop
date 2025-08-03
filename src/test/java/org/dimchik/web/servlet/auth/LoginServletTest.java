package org.dimchik.web.servlet.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.SecurityService;
import org.dimchik.web.session.SessionCookieHandler;
import org.dimchik.web.view.ErrorViewRenderer;
import org.dimchik.web.view.TemplateRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {
    private LoginServlet servlet;

    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Mock
    SecurityService securityService;
    @Mock
    TemplateRenderer templateRenderer;
    @Mock
    ErrorViewRenderer errorViewRenderer;
    @Mock
    SessionCookieHandler sessionCookieHandler;

    @BeforeEach
    void setup() {
        servlet = new LoginServlet(securityService, templateRenderer, errorViewRenderer, sessionCookieHandler);
    }

    @Test
    void doGetShouldRenderLoginTemplate() throws Exception {
        StringWriter writer = new StringWriter();

        when(resp.getWriter()).thenReturn(new PrintWriter(writer));
        when(templateRenderer.processTemplate("login.html")).thenReturn("LOGIN FORM");

        servlet.doGet(req, resp);

        assertTrue(writer.toString().contains("LOGIN FORM"));
    }

    @Test
    void doPostShouldLoginAndSetCookieAndRedirect() throws Exception {
        when(req.getParameter("email")).thenReturn("user@example.com");
        when(req.getParameter("password")).thenReturn("pass123");
        when(securityService.login("user@example.com", "pass123")).thenReturn("TOKEN_123");


        servlet.doPost(req, resp);

        verify(sessionCookieHandler).set(resp, "TOKEN_123");
        verify(resp).sendRedirect("/products");
    }

    @Test
    void doPostShouldRenderErrorOnInvalidCredentials() throws Exception {
        when(req.getParameter("email")).thenReturn("wrong@example.com");
        when(req.getParameter("password")).thenReturn("bad");

        when(securityService.login(any(), any()))
                .thenThrow(new IllegalArgumentException("Invalid email or password"));

        servlet.doPost(req, resp);

        verify(errorViewRenderer).renderBadRequest(resp, "Invalid email or password");
    }
}