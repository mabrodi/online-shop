package org.dimchik.web.servlet.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.entity.User;
import org.dimchik.service.AuthService;
import org.dimchik.service.CartService;

import java.io.IOException;

public class CleanCartServlet extends HttpServlet {
    private final AuthService authService;
    private final CartService cartService;

    public CleanCartServlet(AuthService authService, CartService cartService) {
        this.authService = authService;
        this.cartService = cartService;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = authService.getCurrentUser(req);
        cartService.cleanCartByUserId(user.getId());

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
