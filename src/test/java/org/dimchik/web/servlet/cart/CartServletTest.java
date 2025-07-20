package org.dimchik.web.servlet.cart;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.entity.User;
import org.dimchik.service.AuthService;
import org.dimchik.service.CartService;
import org.dimchik.util.TemplateEngine;
import org.dimchik.web.servlet.product.ProductServlet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServletTest {

    @Mock
    AuthService authService;
    @Mock
    CartService cartService;
    @Mock
    TemplateEngine templateEngine;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Test
    public void doGetWriteHtml() throws Exception {
        CartServlet servlet = new CartServlet(authService, cartService, templateEngine);

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));
        when(templateEngine.processTemplate(anyString(), anyMap())).thenReturn("HTML");

        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("test@example.com");
        user.setPassword("test");

        when(authService.getCurrentUser(request)).thenReturn(user);

        servlet.doGet(request, response);

        verify(cartService).getAllCartsByUserId(1);
        verify(templateEngine).processTemplate(eq("carts.html"), anyMap());
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(sw.toString().contains("HTML"));
    }

}