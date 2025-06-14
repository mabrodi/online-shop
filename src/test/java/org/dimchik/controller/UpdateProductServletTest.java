package org.dimchik.controller;

import org.dimchik.model.Product;
import org.dimchik.service.ProductService;
import org.dimchik.util.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UpdateProductServletTest {
    @Mock ProductService productService;
    @Mock TemplateEngine templateEngine;
    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;

    UpdateProductServlet servlet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new UpdateProductServlet(productService, templateEngine);
    }

    @Test
    public void doGetRendersUpdateForm() throws Exception {
        when(request.getPathInfo()).thenReturn("/8");
        Product product = new Product();
        when(productService.getProductById(8L)).thenReturn(product);
        when(templateEngine.processTemplate(anyString(), anyMap())).thenReturn("UPDATE_FORM");
        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        servlet.doGet(request, response);

        verify(productService).getProductById(8L);
        verify(templateEngine).processTemplate(eq("productFormUpdate.html"), anyMap());
        assertTrue(sw.toString().contains("UPDATE_FORM"));
    }

    @Test
    public void doPutUpdatesProductAndReturnsOk() throws Exception {
        when(request.getPathInfo()).thenReturn("/5");
        when(request.getParameter("name")).thenReturn("UpdatedName");
        when(request.getParameter("price")).thenReturn("800.0");

        servlet.doPut(request, response);

        verify(productService).updateProduct(5L, "UpdatedName", 800.0);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void doPutHandlesInvalidInput() throws Exception {
        when(request.getPathInfo()).thenReturn("/5");
        when(request.getParameter("name")).thenReturn("TV");
        when(request.getParameter("price")).thenReturn("not-a-number");

        servlet.doPut(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), contains("Invalid input"));
    }
}