package org.dimchik.web.servlet.cart;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteCartServletTest {

    @Mock
    CartService cartService;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Test
    public void doDeleteDeletesAndReturnsOk() throws Exception {
        DeleteCartServlet servlet = new DeleteCartServlet(cartService);

        when(request.getPathInfo()).thenReturn("/14");

        servlet.doDelete(request, response);

        verify(cartService).deleteCart(14L);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void doDeleteCartInvalidProductId() throws Exception {
        DeleteCartServlet servlet = new DeleteCartServlet(cartService);

        when(request.getPathInfo()).thenReturn("/not-a-number");


        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> servlet.doDelete(request, response)
        );

        assertTrue(thrown.getMessage().contains("Invalid cart id: not-a-number"));
    }
}