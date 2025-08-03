package org.dimchik.web.servlet.cart;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.context.Session;
import org.dimchik.entity.User;
import org.dimchik.service.CartService;
import org.dimchik.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CleanCartServletTest {
    @Mock
    CartService cartService;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    Session session;

    @Test
    public void doDeleteCleanCartsByUserIdAndReturnsOk() throws Exception {
        when(request.getAttribute("currentSession")).thenReturn(session);

        CleanCartServlet servlet = new CleanCartServlet(cartService);
        servlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

}