package org.dimchik.web.servlet.product;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.entity.Cart;
import org.dimchik.context.Session;
import org.dimchik.entity.User;
import org.dimchik.service.ProductService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddProductServletTest {
    private AddProductServlet servlet;

    @Mock
    ProductService productService;
    @Mock
    TemplateRenderer templateRenderer;
    @Mock
    ErrorViewRenderer errorViewRenderer;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    Session session;
    @Mock
    Cart cart;
    @Mock
    User user;

    @BeforeEach
    void setUp() {
        servlet = new AddProductServlet(productService, templateRenderer, errorViewRenderer);
    }

    @Test
    public void doGetWritesCreateForm() throws Exception {
        when(request.getAttribute("currentSession")).thenReturn(session);
        when(session.getUser()).thenReturn(user);
        when(session.getCart()).thenReturn(cart);
        when(cart.size()).thenReturn(1);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(templateRenderer.processTemplate(anyString(), anyMap())).thenReturn("FORM");

        servlet.doGet(request, response);

        verify(templateRenderer).processTemplate(eq("productFormCreate.html"), anyMap());
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("FORM"));
    }

    @Test
    public void doPostAddsProductAndRedirects() throws Exception {
        when(request.getParameter("name")).thenReturn("TestName");
        when(request.getParameter("price")).thenReturn("150.0");
        when(request.getParameter("description")).thenReturn("text");
        when(request.getContextPath()).thenReturn("");

        servlet.doPost(request, response);

        verify(productService).addProduct("TestName", 150.0, "text");
        verify(response).sendRedirect("/products");
    }

    @Test
    public void doPostHandlesInvalidPriceGracefully() throws Exception {
        when(request.getParameter("name")).thenReturn("TestName");
        when(request.getParameter("price")).thenReturn("invalid");

        servlet.doPost(request, response);

        verify(errorViewRenderer).renderBadRequest(eq(request), eq(response), contains("Invalid input:"));
    }
}