package org.dimchik.web.servlet.cart;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.context.Session;
import org.dimchik.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddCartServletTest {
    private AddCartServlet servlet;

    @Mock
    private CartService cartService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Session session;

    @BeforeEach
    void setUp() {
        servlet = new AddCartServlet(cartService);
    }

    @Test
    void doPostShouldAddProductToCartAndReturnOk() throws Exception {
        when(request.getAttribute("currentSession")).thenReturn(session);
        when(request.getPathInfo()).thenReturn("/42");

        servlet.doPost(request, response);

        verify(cartService).addProduct(session, 42L);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void doPostShouldThrowExceptionOnInvalidProductId() {
        when(request.getAttribute("currentSession")).thenReturn(session);
        when(request.getPathInfo()).thenReturn("/abc");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> servlet.doPost(request, response)
        );

        assertTrue(exception.getMessage().contains("Invalid product id: abc"));
    }
}