package org.dimchik.web.servlet.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dimchik.config.ServiceLocator;
import org.dimchik.service.CartService;
import org.dimchik.context.Session;

import java.io.IOException;

public class DeleteCartServlet extends HttpServlet {
    private final CartService cartService;

    public DeleteCartServlet(CartService cartService) {
        this.cartService = cartService;
    }

    public DeleteCartServlet() {
        this(ServiceLocator.getService(CartService.class));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input = req.getPathInfo().substring(1);
        Session session = (Session) req.getAttribute("currentSession");

        try {
            long productId = Long.parseLong(input);
            cartService.removeProduct(session, productId);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid cart id: " + input, e);
        }
    }
}
