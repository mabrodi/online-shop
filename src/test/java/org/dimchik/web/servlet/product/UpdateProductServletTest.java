package org.dimchik.web.servlet.product;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.entity.Product;
import org.dimchik.service.AuthService;
import org.dimchik.service.ProductService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateProductServletTest {
    @Mock
    AuthService authService;
    @Mock
    ProductService productService;
    @Mock
    TemplateEngine templateEngine;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;


    @Test
    public void doGetRendersUpdateForm() throws Exception {
        UpdateProductServlet servlet = new UpdateProductServlet(authService, productService, templateEngine);

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
        UpdateProductServlet servlet = new UpdateProductServlet(authService, productService, templateEngine);

        when(request.getPathInfo()).thenReturn("/5");
        when(request.getParameter("name")).thenReturn("UpdatedName");
        when(request.getParameter("price")).thenReturn("800.0");
        when(request.getParameter("description")).thenReturn("text");

        servlet.doPut(request, response);

        verify(productService).updateProduct(5, "UpdatedName", 800.0, "text");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void doPutHandlesInvalidInput() throws Exception {
        UpdateProductServlet servlet = new UpdateProductServlet(authService, productService, templateEngine);

        StringWriter stringWriter = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(request.getPathInfo()).thenReturn("/5");
        when(request.getParameter("name")).thenReturn("TV");
        when(request.getParameter("price")).thenReturn("not-a-number");

        servlet.doPut(request, response);

        assertTrue(stringWriter.toString().contains("Invalid input"));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}