package org.dimchik.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteProductServletTest {
    @Mock
    ProductServiceImpl productService;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Test
    public void doDeleteDeletesProductAndReturnsOk() throws Exception {
        DeleteProductServlet servlet = new DeleteProductServlet(productService);

        when(request.getPathInfo()).thenReturn("/12");

        servlet.doDelete(request, response);

        verify(productService).deleteProduct(12L);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void doDeleteHandlesInvalidId() throws Exception {
        DeleteProductServlet servlet = new DeleteProductServlet(productService);

        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        when(request.getPathInfo()).thenReturn("/not-a-number");

        servlet.doDelete(request, response);

        assertTrue(stringWriter.toString().contains("Invalid product id"));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}