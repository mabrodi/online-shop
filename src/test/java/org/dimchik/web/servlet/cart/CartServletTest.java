package org.dimchik.web.servlet.cart;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.context.Cart;
import org.dimchik.context.Session;
import org.dimchik.entity.Product;
import org.dimchik.entity.User;
import org.dimchik.web.view.TemplateRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServletTest {
    @Mock
    private TemplateRenderer templateRenderer;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Session session;
    @Mock
    private Cart cart;
    @Mock
    private User user;

    @Test
    void doGetShouldRenderCartTemplate() throws Exception {
        when(request.getAttribute("currentSession")).thenReturn(session);
        when(session.getUser()).thenReturn(user);
        when(session.getCart()).thenReturn(cart);
        when(cart.size()).thenReturn(1);
        when(cart.getProducts()).thenReturn(Collections.singletonList(new Product()));

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(templateRenderer.processTemplate(eq("carts.html"), anyMap())).thenReturn("CART HTML");

        CartServlet servlet = new CartServlet(templateRenderer);
        servlet.doGet(request, response);

        verify(templateRenderer).processTemplate(eq("carts.html"), anyMap());
        assertTrue(stringWriter.toString().contains("CART HTML"));
    }

}