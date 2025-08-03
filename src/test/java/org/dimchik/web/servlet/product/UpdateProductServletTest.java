package org.dimchik.web.servlet.product;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.context.Cart;
import org.dimchik.context.Session;
import org.dimchik.entity.Product;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductServletTest {
    private UpdateProductServlet servlet;

    @Mock ProductService productService;
    @Mock TemplateRenderer templateRenderer;
    @Mock ErrorViewRenderer errorViewRenderer;
    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;
    @Mock Session session;
    @Mock Cart cart;
    @Mock User user;

    @BeforeEach
    void setUp() {
        servlet = new UpdateProductServlet(productService, templateRenderer, errorViewRenderer);
    }

    @Test
    void doGetRendersUpdateForm() throws Exception {
        when(request.getAttribute("currentSession")).thenReturn(session);
        when(session.getUser()).thenReturn(user);
        when(session.getCart()).thenReturn(cart);
        when(cart.size()).thenReturn(3);

        when(request.getPathInfo()).thenReturn("/8");
        Product product = new Product();
        when(productService.getProductById(8L)).thenReturn(product);
        when(templateRenderer.processTemplate(anyString(), anyMap())).thenReturn("UPDATE_FORM");

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        servlet.doGet(request, response);

        verify(productService).getProductById(8L);
        verify(templateRenderer).processTemplate(eq("productFormUpdate.html"), anyMap());
        assertTrue(sw.toString().contains("UPDATE_FORM"));
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void doPostUpdatesProductAndReturnsOk() throws Exception {
        when(request.getPathInfo()).thenReturn("/5");
        when(request.getParameter("name")).thenReturn("UpdatedName");
        when(request.getParameter("price")).thenReturn("800.0");
        when(request.getParameter("description")).thenReturn("New description");

        servlet.doPost(request, response);

        verify(productService).updateProduct(5L, "UpdatedName", 800.0, "New description");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void doPostHandlesInvalidPrice() throws Exception {
        when(request.getPathInfo()).thenReturn("/5");
        when(request.getParameter("name")).thenReturn("UpdatedName");
        when(request.getParameter("price")).thenReturn("bad-price");

        servlet.doPost(request, response);

        verify(errorViewRenderer).renderBadRequest(eq(request), eq(response), contains("Invalid input"));
    }

    @Test
    void doGetHandlesInvalidProductId() throws Exception {
        when(request.getPathInfo()).thenReturn("/not-a-number");
        when(request.getAttribute("currentSession")).thenReturn(session);

        servlet.doGet(request, response);

        verify(errorViewRenderer).renderBadRequest(eq(request), eq(response), eq("Invalid product id"));
    }
}