package org.dimchik.web.servlet.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.service.CartService;
import org.dimchik.util.ErrorRendererUtil;

import java.io.IOException;

public class DeleteCartServlet extends HttpServlet {
    private final CartService cartService;

    public DeleteCartServlet(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input = req.getPathInfo().substring(1);
        try {
            long id = Long.parseLong(input);
            cartService.deleteCart(id);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid cart id: " + input, e);
        }
    }
}
