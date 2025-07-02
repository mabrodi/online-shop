package org.dimchik.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.impl.ProductServiceImpl;
import org.dimchik.util.TemplateEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServletTest {
    @Mock
    ProductServiceImpl productService;
    @Mock
    TemplateEngine templateEngine;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Test
    public void doGetWriteHtml() throws Exception {
        ProductServlet servlet = new ProductServlet(productService, templateEngine);

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));
        when(templateEngine.processTemplate(anyString(), anyMap())).thenReturn("HTML");

        servlet.doGet(request, response);

        verify(templateEngine).processTemplate(eq("products.html"), anyMap());
        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(sw.toString().contains("HTML"));
    }

    @Test
    void doGetWithSearchShouldCallSearchProductsAndPassQuery() throws Exception {
        ProductServlet servlet = new ProductServlet(productService, templateEngine);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(request.getParameter("search")).thenReturn("Apple");
        when(templateEngine.processTemplate(anyString(), anyMap())).thenReturn("HTML");

        servlet.doGet(request, response);

        verify(productService).searchProducts("Apple");

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(templateEngine).processTemplate(eq("products.html"), captor.capture());

        Map<String, Object> data = captor.getValue();
        assertEquals("Apple", data.get("query"));

        verify(response).setContentType("text/html;charset=utf-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(stringWriter.toString().contains("HTML"));
    }
}