package org.dimchik.web.servlet.product;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.context.Cart;
import org.dimchik.context.Session;
import org.dimchik.entity.User;
import org.dimchik.service.ProductService;
import org.dimchik.service.SecurityService;
import org.dimchik.web.view.TemplateRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServletTest {
    private ProductServlet servlet;

    @Mock
    ProductService productService;
    @Mock
    TemplateRenderer templateRenderer;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    Session session;
    @Mock
    User user;
    @Mock
    Cart cart;

    @BeforeEach
    void setUp() {
        servlet = new ProductServlet(productService, templateRenderer);
    }

    @Test
    public void doGetWriteHtml() throws Exception {
        when(request.getAttribute("currentSession")).thenReturn(session);
        when(session.getUser()).thenReturn(user);
        when(session.getCart()).thenReturn(cart);
        when(cart.size()).thenReturn(3);

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));
        when(templateRenderer.processTemplate(anyString(), anyMap())).thenReturn("HTML");

        servlet.doGet(request, response);

        verify(templateRenderer).processTemplate(eq("products.html"), anyMap());
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(sw.toString().contains("HTML"));
    }

    @Test
    void doGetWithSearchShouldCallSearchProductsAndPassQuery() throws Exception {
        when(request.getAttribute("currentSession")).thenReturn(session);
        when(session.getUser()).thenReturn(user);
        when(session.getCart()).thenReturn(cart);
        when(cart.size()).thenReturn(1);

        when(request.getParameter("search")).thenReturn("Apple");

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(templateRenderer.processTemplate(anyString(), anyMap())).thenReturn("HTML");

        servlet.doGet(request, response);

        verify(productService).searchProducts("Apple");

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(templateRenderer).processTemplate(eq("products.html"), captor.capture());

        Map<String, Object> data = captor.getValue();
        assertEquals("Apple", data.get("query"));

        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("HTML"));
    }
}