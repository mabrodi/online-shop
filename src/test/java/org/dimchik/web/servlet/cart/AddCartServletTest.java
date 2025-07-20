package org.dimchik.web.servlet.cart;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.entity.User;
import org.dimchik.service.AuthService;
import org.dimchik.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddCartServletTest {

    @Mock
    AuthService authService;
    @Mock
    CartService cartService;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Test
    public void doPostAddCartAndReturnsOk() throws Exception {
        AddCartServlet servlet = new AddCartServlet(authService, cartService);

        when(request.getPathInfo()).thenReturn("/1");
        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("test@example.com");
        user.setPassword("test");

        when(authService.getCurrentUser(request)).thenReturn(user);

        servlet.doPost(request, response);

        verify(cartService).addCart(user.getId(), 1);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void doAddHandlesInvalidProductId() throws Exception {
        AddCartServlet servlet = new AddCartServlet(authService, cartService);

        when(request.getPathInfo()).thenReturn("/not-a-number");


        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> servlet.doPost(request, response)
        );

        assertTrue(thrown.getMessage().contains("Invalid product id: not-a-number"));
    }
}