package org.dimchik.controller;

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

class AddProductServletTest {
    @Mock ProductService productService;
    @Mock TemplateEngine templateEngine;
    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;

    AddProductServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new AddProductServlet(productService, templateEngine);
    }

    @Test
    void doGetWritesCreateForm() throws Exception {
        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));
        when(templateEngine.processTemplate(anyString(), anyMap())).thenReturn("FORM");

        servlet.doGet(request, response);

        verify(templateEngine).processTemplate(eq("productFormCreate.html"), anyMap());
        assertTrue(sw.toString().contains("FORM"));
    }

    @Test
    void doPostAddsProductAndRedirects() throws Exception {
        when(request.getParameter("name")).thenReturn("TestName");
        when(request.getParameter("price")).thenReturn("150.0");

        servlet.doPost(request, response);

        verify(productService).addProduct("TestName", 150.0);
        verify(response).sendRedirect("/products");
    }
}