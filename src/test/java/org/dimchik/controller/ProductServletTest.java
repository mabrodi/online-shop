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

class ProductServletTest {
    @Mock
    ProductService productService;
    @Mock
    TemplateEngine templateEngine;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    ProductServlet productServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productServlet = new ProductServlet(productService, templateEngine);
    }

    @Test
    void doGetWritesHtml() throws Exception {
        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));
        when(templateEngine.processTemplate(anyString(), anyMap())).thenReturn("HTML");

        productServlet.doGet(request, response);

        verify(templateEngine).processTemplate(eq("products.html"), anyMap());
        assertTrue(sw.toString().contains("HTML"));
    }
}