package org.dimchik.web.servlet.product;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteProductServletTest {
    @Mock
    ProductService productService;
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

        when(request.getPathInfo()).thenReturn("/not-a-number");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> servlet.doDelete(request, response)
        );

        assertTrue(thrown.getMessage().contains("Invalid product id: not-a-number"));
    }
}