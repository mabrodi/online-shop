package org.dimchik.web.servlet.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.entity.User;
import org.dimchik.service.AuthService;
import org.dimchik.service.CartService;
import org.dimchik.util.ErrorRendererUtil;

import java.io.IOException;

public class AddCartServlet extends HttpServlet {
    private final AuthService authService;
    private final CartService cartService;

    public AddCartServlet(AuthService authService, CartService cartService) {
        this.authService = authService;
        this.cartService = cartService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input = req.getPathInfo().substring(1);
        try {
            long productId = Long.parseLong(input);
            User user = authService.getCurrentUser(req);
            cartService.addCart(user.getId(), productId);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid product id: " + input, e);
        }
    }
}
