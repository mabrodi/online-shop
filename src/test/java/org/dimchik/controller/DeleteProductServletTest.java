package org.dimchik.controller;

import org.dimchik.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

class DeleteProductServletTest {
    @Mock ProductService productService;
    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;

    DeleteProductServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new DeleteProductServlet(productService);
    }

    @Test
    void doDeleteDeletesProductAndReturnsOk() throws Exception {
        when(request.getPathInfo()).thenReturn("/12");

        servlet.doDelete(request, response);

        verify(productService).deleteProduct(12L);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}