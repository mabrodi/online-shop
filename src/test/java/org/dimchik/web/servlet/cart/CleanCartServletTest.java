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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CleanCartServletTest {
    @Mock
    AuthService authService;
    @Mock
    CartService cartService;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Test
    public void doDeleteCleanCartsByUserIdAndReturnsOk() throws Exception {
        CleanCartServlet servlet = new CleanCartServlet(authService, cartService);

        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("test@example.com");
        user.setPassword("test");

        when(authService.getCurrentUser(request)).thenReturn(user);

        servlet.doDelete(request, response);

        verify(cartService).cleanCartByUserId(user.getId());
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

}