package org.dimchik.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.impl.AuthServiceImpl;
import org.dimchik.service.impl.ProductServiceImpl;
import org.dimchik.util.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddProductServletTest {

    @Mock
    ProductServiceImpl productService;
    @Mock
    AuthServiceImpl authService;
    @Mock
    TemplateEngine templateEngine;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Test
    public void doGetWritesCreateForm() throws Exception {
        AddProductServlet servlet = new  AddProductServlet(authService, productService, templateEngine);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(templateEngine.processTemplate(anyString(), anyMap())).thenReturn("FORM");

        servlet.doGet(request, response);

        verify(templateEngine).processTemplate(eq("productFormCreate.html"), anyMap());
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("FORM"));
    }

    @Test
    public void doPostAddsProductAndRedirects() throws Exception {
        AddProductServlet servlet = new  AddProductServlet(authService, productService, templateEngine);

        when(request.getParameter("name")).thenReturn("TestName");
        when(request.getParameter("price")).thenReturn("150.0");
        when(request.getParameter("description")).thenReturn("text");

        servlet.doPost(request, response);

        verify(productService).addProduct("TestName", 150.0, "text");
        verify(response).sendRedirect("/products");
    }

    @Test
    public void doPostHandlesInvalidPriceGracefully() throws Exception {
        AddProductServlet servlet = new  AddProductServlet(authService, productService, templateEngine);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        when(request.getParameter("name")).thenReturn("TestName");
        when(request.getParameter("price")).thenReturn("invalid");

        servlet.doPost(request, response);

        assertTrue(stringWriter.toString().contains("Invalid input"));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}