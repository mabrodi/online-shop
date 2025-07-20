package org.dimchik.web.servlet.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.dimchik.entity.User;
import org.dimchik.service.AuthService;
import org.dimchik.service.UserService;
import org.dimchik.service.impl.AuthServiceImpl;
import org.dimchik.util.TemplateEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {
    @Mock
    UserService userService;
    @Mock
    TemplateEngine templateEngine;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;


    @Test
    public void doGetWritesCreateForm() throws Exception {
        AuthService authService =  new AuthServiceImpl();
        LoginServlet servlet = new LoginServlet(authService, templateEngine, userService);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(templateEngine.processTemplate(anyString(), anyMap())).thenReturn("FORM");

        servlet.doGet(request, response);

        verify(templateEngine).processTemplate(eq("login.html"), anyMap());
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("FORM"));
    }

    @Test
    public void doPostShouldLoginBySessionAndRedirect() throws Exception {
        AuthService authService =  new AuthServiceImpl();
        LoginServlet servlet = new LoginServlet(authService, templateEngine, userService);

        User user = new User();
        when(request.getParameter("email")).thenReturn("email@example.com");
        when(request.getParameter("password")).thenReturn("11111");
        when(userService.login("email@example.com", "11111")).thenReturn(user);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(session).setAttribute("user", user);
        verify(response).sendRedirect("/products");
    }

    @Test
    void doPostShouldHandleInvalidLogin() throws Exception {
        AuthService authService =  new AuthServiceImpl();
        LoginServlet servlet = new LoginServlet(authService, templateEngine, userService);

        when(request.getParameter("email")).thenReturn("email@example.com");
        when(request.getParameter("password")).thenReturn("wrong");

        when(userService.login(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Invalid credentials"));

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        servlet.doPost(request, response);

        assertTrue(stringWriter.toString().contains("Invalid email or password"));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}